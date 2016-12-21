%%%%%%%%%%%%%%%%%%%%%%%%%%
%
% implement the function that 
% analysis the movies
% and show the hot list.
% @time 2016/12/2
% @author duocai
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

:- module(hots,[
		hot/0,hot/1
	]).

hot(Num) :- 
	get_all_evaluation(All),
	quick_sort(All,Sorted),
	(
		%show detail infos if the Num < 4
		(
			Num < 4,
			print_movie_detail(Sorted,0,Num)
		);
		%else show brief info for there are too many movies
		(
			Num > 3,
			print_movie_brief(Sorted,0,Num)
		)
	);
	1=1.%suss

%default hot list	
hot :- hot(3).

print_movie(All,Num) :- 
	Num > 0,
	[Movie|Rest] = All,
	nth0(0,Movie,Name),
	show(Name),
	print_movie(Rest,Num-1).

%show brief infos of the movies	
print_movie_brief(All,Index,Num) :- 
	Index < Num,
	nth0(Index,All,Movie),
	nth0(0,Movie,Name),
	Next is (Index+1),
	format('第~d部',[Next]),nl,%Index start from 0
	nth0(1,Movie,Eval),
	write('综合指标':Eval),nl,
	write('电影':Name),nl,
	info(Name,Plot),
	shows:show_plot(Plot),nl,
	print_movie_brief(All,Next,Num).%recusion to print all

%show datial infos of the movies
print_movie_detail(All,Index,Num) :- 
	Index < Num,
	nth0(Index,All,Movie),
	nth0(0,Movie,Name),
	Next is (Index+1),
	format('第~d部',[Next]),nl,%Index start from 0
	nth0(1,Movie,Eval),
	write('综合指标':Eval),nl,
	show(Name),nl,
	print_movie_detail(All,Next,Num).%recusion to print all

get_all_evaluation(All) :- 
	findall([Name,Eval],find_one([Name,Eval]),All).

find_one([Name,Eval]):-
	movie(Name,_,_,_,_,_,Score,Num,_),
	%calculate the evaluation socre, can be extended more complex in future.
	Eval is (Score + Num*0.00001).

%implement the specific quick sort for this templete: H=[Name,Score]
quick_sort(List,Sorted):-q_sort(List,[],Sorted).
q_sort([],Acc,Acc).
q_sort([H|T],Acc,Sorted):-
    pivoting(H,T,L1,L2),
    q_sort(L1,Acc,Sorted1),
    q_sort(L2,[H|Sorted1],Sorted).
    
pivoting(_,[],[],[]).
pivoting(H,[X|T],[X|L],G):-
	%note that H=[Name,Score]
	nth0(1,X,S1), %built-in rule, index start from 0
	nth0(1,H,S2),
	S1 =< S2,
	pivoting(H,T,L,G).
pivoting(H,[X|T],L,[X|G]):-
	%note that H=[Name,Score]
	nth0(1,X,S1),
	nth0(1,H,S2),
	S1 > S2,
	pivoting(H,T,L,G).