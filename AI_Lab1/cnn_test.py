###########################################
# @Author duocai
# @Date 2016/10/12
# @Time 21:10
###########################################

import numpy as np;
import tensorflow as tf

test_len = 3200

# super parameters
input_img_width = 28
input_size = 784 # 28*28

# convolution and pooling 1
patch_size_conv1 = 5;
feature_num_conv1 = 32
stride_conv = 1
padding_conv = 'VALID'
size_pool = 2
stride_pool = 2
padding_pool = 'VALID'

# convolution and pooling 2
feature_num_conv2 = 64
patch_size_conv2 = 5
# stride_conv2 = 1
# padding_conv2 = 'VALID'
# stride_pool2 = 2
# padding_pool2 = 'VALID'

# full connection 1
size_fc1 = 1024 # similar like hidden layer size

# dropout
dropout_p = 0.38

# output layer size
output_size = 8

offset = 0 # batch window offset

# load dataset
def load_data(path,lens):
    data = np.fromfile(path,dtype = np.float,sep = ' ').reshape(lens,input_size)/255.0
    return data

test = load_data('test.txt',test_len)

# input data
x = tf.placeholder(tf.float32, [None, input_size])

# convolutional nerual net
# Convolution and Pooling
def conv2d(x, W):
  return tf.nn.conv2d(x, W, strides=[1, stride_conv, stride_conv, 1], padding=padding_conv)

def max_pool_2x2(x):
  return tf.nn.max_pool(x, ksize=[1, size_pool, size_pool, 1],
                        strides=[1, stride_pool, stride_pool, 1], padding=padding_pool)

# load weigths
weights = np.load('weights.npz')

# First Convolutional Layer
W_conv1 = weights['arr_0']
b_conv1 = weights['arr_1']
x_image = tf.reshape(x, [-1,28,28,1])
h_conv1 = tf.nn.relu(conv2d(x_image, W_conv1) + b_conv1)
h_pool1 = max_pool_2x2(h_conv1)

# Second Convolutional Layer
W_conv2 = weights['arr_2']
b_conv2 = weights['arr_3']

h_conv2 = tf.nn.relu(conv2d(h_pool1, W_conv2) + b_conv2)
h_pool2 = max_pool_2x2(h_conv2)

# Densely Connected Layer
image_width = ((input_img_width-patch_size_conv1+1)/2 - patch_size_conv2 + 1)/2 # get image witdh now
W_fc1 = weights['arr_4']
b_fc1 = weights['arr_5']

# change to 1D
h_pool2_flat = tf.reshape(h_pool2, [-1, image_width*image_width * feature_num_conv2])

# dropout 1
keep_prob = tf.placeholder(tf.float32)
h_pool2_drop = tf.nn.dropout(h_pool2_flat, keep_prob)

# get hidden layer output
h_fc1 = tf.nn.relu(tf.matmul(h_pool2_drop, W_fc1) + b_fc1)

# Dropout 2
h_fc1_drop = tf.nn.dropout(h_fc1, keep_prob)

# Readout layer
W_fc2 = weights['arr_6']
b_fc2 = weights['arr_7']
y_conv = tf.matmul(h_fc1_drop, W_fc2) + b_fc2

# get result
result_op = tf.argmax(y_conv,1)

# get session
sess = tf.InteractiveSession()
sess.run(tf.initialize_all_variables())


# the test set is too large, so I need to divide it. 
# and test it with every about 1500-size part
# Otherwise, the RAM(2GB) of the virtual machine will be error because the RAM can't hold so large dataset
part_len = 1600
part_num = 2
output = open('LetterCNN14302010040.txt','w')
for i in range(part_num):
  j = i*part_len;
  k = (i+1)*part_len
  result = result_op.eval(feed_dict={x:test[j:k,:], keep_prob: 1.0})
  for m in range(part_len):
    output.write(chr(65+result[m])) # A:65
    output.write("\r\n")
output.close()
print('finished.')
