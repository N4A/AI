# -*- coding: utf-8 -*-
"""
Created on Mon Dec  5 16:23:29 2016

@author: admin
"""
 
import sys  
''''' 
这个函数的作用是，从区间的第一个，最后一个和最中间的位置上选出一个中间大小的值，并把它放置在区间的第一个位置上 
这样有效消除预排序的最坏情况 
'''  
def median(a,start,end):  
    center=(start+end)/2  
    if a[start]>a[center]:  
        a[start],a[center]=a[center],a[start]  
    if a[start]>a[end]:  
        a[start],a[end]=a[end],a[start]  
    if a[center]>a[end]:  
        a[center],a[end]=a[end],a[center]  
    a[start],a[center]=a[center],a[start]  
def doSwap(a,start,end):  
    if start>=end:  
        return  
    i,j=start,end  
    median(a,start,end)  
    tmp=a[start]  
    while(True):  
        while(a[j]>tmp and i<j):  
            j-=1  
        if i<j:  
            a[i]=a[j]  
            i+=1  
        while(a[i]<tmp and i<j):  
            i+=1  
        if i<j:  
            a[j]=a[i]  
            j-=1  
        else:  
            break  
    a[i]=tmp  
    doSwap(a,start,i-1)  
    doSwap(a,j+1,end)  
def quickSort(a):  
    #设置递归深度为10000000，放置数据量过大时超出递归最大深度发生exception  
    sys.setrecursionlimit(1000000)  
    doSwap(a,0,len(a)-1)