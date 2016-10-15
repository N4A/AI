

```python
###########################################
# @Author duocai
# @Date 2016/10/12
# @Time 21:10
###########################################

import numpy as np;
import tensorflow as tf

train_len = 7780
validation_len = 3200

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
dropout_p = 0.35

# output layer size
output_size = 8

offset = 0 # batch window offset

# load dataset
def load_data(path,lens):
    data = np.fromfile(path,dtype = np.float,sep = ' ').reshape(lens,input_size+1)
    # get input
    data_i = data[:,0:-1]/255.0
    #get output
    out_t = data[:,-1].astype(np.int)
    data_o = np.zeros([train_len,output_size],np.float)
    for i in range(lens):
        data_o[i,out_t[i]] = 1
    return [data_i,data_o]
def next_batch(size):
    global offset
    i = train[0]
    o = train[1]
    if (offset+size <= train_len):
        offset += size
        return [i[offset-size:offset,:],o[offset-size:offset,:]]
    data = [i[offset:train_len,:],o[offset:train_len,:]]
    offset = 0
    return data
    

train = load_data('trains.txt',train_len)
validation = load_data('validation.txt',validation_len)

# input data
x = tf.placeholder(tf.float32, [None, input_size])

# output labels
y_ = tf.placeholder(tf.float32, [None, output_size])

# convolutional nerual net
# Weight Initialization
def weight_variable(shape):
  initial = tf.truncated_normal(shape, stddev=0.1)
  return tf.Variable(initial)

def bias_variable(shape):
  initial = tf.constant(0.1, shape=shape)
  return tf.Variable(initial)

# Convolution and Pooling
def conv2d(x, W):
  return tf.nn.conv2d(x, W, strides=[1, stride_conv, stride_conv, 1], padding=padding_conv)

def max_pool_2x2(x):
  return tf.nn.max_pool(x, ksize=[1, size_pool, size_pool, 1],
                        strides=[1, stride_pool, stride_pool, 1], padding=padding_pool)

# First Convolutional Layer
W_conv1 = weight_variable([patch_size_conv1, patch_size_conv1, 1, feature_num_conv1])
b_conv1 = bias_variable([feature_num_conv1])
x_image = tf.reshape(x, [-1,28,28,1])
h_conv1 = tf.nn.relu(conv2d(x_image, W_conv1) + b_conv1)
h_pool1 = max_pool_2x2(h_conv1)

# Second Convolutional Layer
W_conv2 = weight_variable([patch_size_conv2, patch_size_conv2, feature_num_conv1, feature_num_conv2])
b_conv2 = bias_variable([feature_num_conv2])

h_conv2 = tf.nn.relu(conv2d(h_pool1, W_conv2) + b_conv2)
h_pool2 = max_pool_2x2(h_conv2)

# Densely Connected Layer
image_width = ((input_img_width-patch_size_conv1+1)/2 - patch_size_conv2 + 1)/2 # get image witdh now
W_fc1 = weight_variable([image_width*image_width * feature_num_conv2, size_fc1])
b_fc1 = bias_variable([size_fc1])

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
W_fc2 = weight_variable([size_fc1, output_size])
b_fc2 = bias_variable([output_size])

y_conv = tf.matmul(h_fc1_drop, W_fc2) + b_fc2

# train and evaluate model
cross_entropy = tf.reduce_mean(tf.nn.softmax_cross_entropy_with_logits(y_conv, y_))
train_step = tf.train.AdamOptimizer(1e-4).minimize(cross_entropy)
correct_prediction = tf.equal(tf.argmax(y_conv,1), tf.argmax(y_,1))
accuracy = tf.reduce_mean(tf.cast(correct_prediction, tf.float32))

sess = tf.InteractiveSession()
sess.run(tf.initialize_all_variables())
for i in range(40001):
  batch = next_batch(50)
  if i%100 == 0:
    train_accuracy = accuracy.eval(feed_dict={
        x:batch[0], y_: batch[1], keep_prob: 1.0})
    print("step %d, training accuracy %g"%(i, train_accuracy))
  train_step.run(feed_dict={x: batch[0], y_: batch[1], keep_prob: dropout_p})
  if i%100 == 0 and i > 10000:
        test_accuracy = 0
        # the test set is too large, so I need to divide it. 
        # and test it with every about 1500-size part
        # Otherwise, the RAM(2GB) of the virtual machine will be error because the RAM can't hold so large dataset
        for i in range(2):
          j = i*1600;
          k = (i+1)*1600
          test_accuracy += accuracy.eval(feed_dict={x:validation[0][j:k,:],y_: validation[1][j:k,:], keep_prob: 1.0})
        test_accuracy /= 2;
        print("test accuracy %g"% test_accuracy)

test_accuracy = 0
# the test set is too large, so I need to divide it. 
# and test it with every about 1500-size part
# Otherwise, the RAM(2GB) of the virtual machine will be error because the RAM can't hold so large dataset
for i in range(2):
  j = i*1600;
  k = (i+1)*1600
  test_accuracy += accuracy.eval(feed_dict={x:validation[0][j:k,:],y_: validation[1][j:k,:], keep_prob: 1.0})
test_accuracy /= 2;
print("test accuracy %g"% test_accuracy)

# output weights
np.savez('weigths2.npz',W_conv1.eval(),b_conv1.eval(),W_conv2.eval(),b_conv2.eval(),W_fc1.eval(),b_fc1.eval(),W_fc2.eval(),b_fc2.eval())
```

    Exception AssertionError: AssertionError() in <bound method InteractiveSession.__del__ of <tensorflow.python.client.session.InteractiveSession object at 0x7f1baf358190>> ignored


    step 0, training accuracy 0.12
    step 100, training accuracy 0.5
    step 200, training accuracy 0.66
    step 300, training accuracy 0.74
    step 400, training accuracy 0.82
    step 500, training accuracy 0.8
    step 600, training accuracy 0.78
    step 700, training accuracy 0.78
    step 800, training accuracy 0.72
    step 900, training accuracy 0.9
    step 1000, training accuracy 0.84
    step 1100, training accuracy 0.94
    step 1200, training accuracy 0.88
    step 1300, training accuracy 0.98
    step 1400, training accuracy 0.9
    step 1500, training accuracy 0.84
    step 1600, training accuracy 0.94
    step 1700, training accuracy 0.9
    step 1800, training accuracy 0.76
    step 1900, training accuracy 0.88
    step 2000, training accuracy 0.9
    step 2100, training accuracy 0.94
    step 2200, training accuracy 0.9
    step 2300, training accuracy 0.94
    step 2400, training accuracy 0.96
    step 2500, training accuracy 0.82
    step 2600, training accuracy 0.92
    step 2700, training accuracy 0.9
    step 2800, training accuracy 0.9
    step 2900, training accuracy 0.96
    step 3000, training accuracy 0.92
    step 3100, training accuracy 0.86
    step 3200, training accuracy 0.96
    step 3300, training accuracy 0.88
    step 3400, training accuracy 0.98
    step 3500, training accuracy 0.92
    step 3600, training accuracy 0.94
    step 3700, training accuracy 0.96
    step 3800, training accuracy 0.88
    step 3900, training accuracy 0.98
    step 4000, training accuracy 0.9
    step 4100, training accuracy 0.9
    step 4200, training accuracy 0.9
    step 4300, training accuracy 0.96
    step 4400, training accuracy 0.92
    step 4500, training accuracy 0.9
    step 4600, training accuracy 0.96
    step 4700, training accuracy 0.84
    step 4800, training accuracy 0.94
    step 4900, training accuracy 0.96
    step 5000, training accuracy 1
    step 5100, training accuracy 0.92
    step 5200, training accuracy 0.98
    step 5300, training accuracy 0.96
    step 5400, training accuracy 0.92
    step 5500, training accuracy 0.92
    step 5600, training accuracy 0.92
    step 5700, training accuracy 0.84
    step 5800, training accuracy 0.88
    step 5900, training accuracy 0.94
    step 6000, training accuracy 1
    step 6100, training accuracy 0.96
    step 6200, training accuracy 0.94
    step 6300, training accuracy 0.96
    step 6400, training accuracy 0.88
    step 6500, training accuracy 0.92
    step 6600, training accuracy 0.94
    step 6700, training accuracy 0.98
    step 6800, training accuracy 0.98
    step 6900, training accuracy 0.92
    step 7000, training accuracy 0.92
    step 7100, training accuracy 0.98
    step 7200, training accuracy 0.94
    step 7300, training accuracy 0.98
    step 7400, training accuracy 0.94
    step 7500, training accuracy 0.98
    step 7600, training accuracy 0.96
    step 7700, training accuracy 0.96
    step 7800, training accuracy 0.98
    step 7900, training accuracy 0.94
    step 8000, training accuracy 0.94
    step 8100, training accuracy 0.92
    step 8200, training accuracy 0.96
    step 8300, training accuracy 0.94
    step 8400, training accuracy 0.92
    step 8500, training accuracy 1
    step 8600, training accuracy 0.92
    step 8700, training accuracy 0.94
    step 8800, training accuracy 0.98
    step 8900, training accuracy 1
    step 9000, training accuracy 0.94
    step 9100, training accuracy 0.98
    step 9200, training accuracy 0.98
    step 9300, training accuracy 0.98
    step 9400, training accuracy 0.96
    step 9500, training accuracy 0.98
    step 9600, training accuracy 0.86
    step 9700, training accuracy 0.92
    step 9800, training accuracy 0.98
    step 9900, training accuracy 1
    step 10000, training accuracy 0.96
    step 10100, training accuracy 0.96
    test accuracy 0.93375
    step 10200, training accuracy 0.98
    test accuracy 0.9325
    step 10300, training accuracy 0.96
    test accuracy 0.931563
    step 10400, training accuracy 0.94
    test accuracy 0.934063
    step 10500, training accuracy 0.94
    test accuracy 0.935313
    step 10600, training accuracy 0.96
    test accuracy 0.936562
    step 10700, training accuracy 0.98
    test accuracy 0.936563
    step 10800, training accuracy 0.92
    test accuracy 0.933125
    step 10900, training accuracy 0.94
    test accuracy 0.93375
    step 11000, training accuracy 1
    test accuracy 0.9375
    step 11100, training accuracy 0.94
    test accuracy 0.93625
    step 11200, training accuracy 1
    test accuracy 0.935625
    step 11300, training accuracy 0.94
    test accuracy 0.936563
    step 11400, training accuracy 0.98
    test accuracy 0.93375
    step 11500, training accuracy 1
    test accuracy 0.93875
    step 11600, training accuracy 0.98
    test accuracy 0.938125
    step 11700, training accuracy 0.98
    test accuracy 0.938438
    step 11800, training accuracy 0.94
    test accuracy 0.937187
    step 11900, training accuracy 0.94
    test accuracy 0.936563
    step 12000, training accuracy 0.94
    test accuracy 0.938437
    step 12100, training accuracy 1
    test accuracy 0.937187
    step 12200, training accuracy 0.94
    test accuracy 0.935
    step 12300, training accuracy 0.94
    test accuracy 0.937813
    step 12400, training accuracy 1
    test accuracy 0.938437
    step 12500, training accuracy 0.94
    test accuracy 0.936875
    step 12600, training accuracy 0.98
    test accuracy 0.939375
    step 12700, training accuracy 0.98
    test accuracy 0.936562
    step 12800, training accuracy 1
    test accuracy 0.93875
    step 12900, training accuracy 0.94
    test accuracy 0.938437
    step 13000, training accuracy 1
    test accuracy 0.9375
    step 13100, training accuracy 1
    test accuracy 0.938438
    step 13200, training accuracy 0.98
    test accuracy 0.940313
    step 13300, training accuracy 0.94
    test accuracy 0.9375
    step 13400, training accuracy 0.98
    test accuracy 0.939687
    step 13500, training accuracy 0.9
    test accuracy 0.94
    step 13600, training accuracy 0.96
    test accuracy 0.9375
    step 13700, training accuracy 1
    test accuracy 0.936875
    step 13800, training accuracy 1
    test accuracy 0.942813
    step 13900, training accuracy 0.96
    test accuracy 0.938125
    step 14000, training accuracy 0.96
    test accuracy 0.938437
    step 14100, training accuracy 0.98
    test accuracy 0.93875
    step 14200, training accuracy 1
    test accuracy 0.9425
    step 14300, training accuracy 0.96
    test accuracy 0.941875
    step 14400, training accuracy 0.94
    test accuracy 0.938125
    step 14500, training accuracy 0.98
    test accuracy 0.94
    step 14600, training accuracy 1
    test accuracy 0.94
    step 14700, training accuracy 0.96
    test accuracy 0.938438
    step 14800, training accuracy 0.98
    test accuracy 0.94
    step 14900, training accuracy 1
    test accuracy 0.939375
    step 15000, training accuracy 0.98
    test accuracy 0.938437
    step 15100, training accuracy 1
    test accuracy 0.939688
    step 15200, training accuracy 0.98
    test accuracy 0.940625
    step 15300, training accuracy 0.98
    test accuracy 0.940625
    step 15400, training accuracy 1
    test accuracy 0.939687
    step 15500, training accuracy 0.98
    test accuracy 0.941563
    step 15600, training accuracy 0.98
    test accuracy 0.941875
    step 15700, training accuracy 0.94
    test accuracy 0.941875
    step 15800, training accuracy 0.98
    test accuracy 0.938438
    step 15900, training accuracy 0.98
    test accuracy 0.94375
    step 16000, training accuracy 1
    test accuracy 0.939063
    step 16100, training accuracy 0.96
    test accuracy 0.942188
    step 16200, training accuracy 0.94
    test accuracy 0.941875
    step 16300, training accuracy 1
    test accuracy 0.939687
    step 16400, training accuracy 0.96
    test accuracy 0.939063
    step 16500, training accuracy 1
    test accuracy 0.9425
    step 16600, training accuracy 1
    test accuracy 0.942188
    step 16700, training accuracy 1
    test accuracy 0.942813
    step 16800, training accuracy 0.96
    test accuracy 0.942813
    step 16900, training accuracy 1
    test accuracy 0.939375
    step 17000, training accuracy 1
    test accuracy 0.943125
    step 17100, training accuracy 0.98
    test accuracy 0.941563
    step 17200, training accuracy 0.96
    test accuracy 0.941563
    step 17300, training accuracy 1
    test accuracy 0.943125
    step 17400, training accuracy 0.96
    test accuracy 0.942187
    step 17500, training accuracy 0.96
    test accuracy 0.940313
    step 17600, training accuracy 1
    test accuracy 0.942813
    step 17700, training accuracy 1
    test accuracy 0.940937
    step 17800, training accuracy 0.98
    test accuracy 0.943437
    step 17900, training accuracy 0.98
    test accuracy 0.94375
    step 18000, training accuracy 0.98
    test accuracy 0.943438
    step 18100, training accuracy 1
    test accuracy 0.945
    step 18200, training accuracy 0.98
    test accuracy 0.945312
    step 18300, training accuracy 0.96
    test accuracy 0.942813
    step 18400, training accuracy 0.98
    test accuracy 0.945625
    step 18500, training accuracy 1
    test accuracy 0.944688
    step 18600, training accuracy 0.98
    test accuracy 0.942187
    step 18700, training accuracy 0.98
    test accuracy 0.94
    step 18800, training accuracy 1
    test accuracy 0.944688
    step 18900, training accuracy 1
    test accuracy 0.943125
    step 19000, training accuracy 1
    test accuracy 0.940937
    step 19100, training accuracy 0.98
    test accuracy 0.941563
    step 19200, training accuracy 0.98
    test accuracy 0.942188
    step 19300, training accuracy 1
    test accuracy 0.946562
    step 19400, training accuracy 1
    test accuracy 0.943438
    step 19500, training accuracy 1
    test accuracy 0.945625
    step 19600, training accuracy 0.96
    test accuracy 0.945
    step 19700, training accuracy 0.96
    test accuracy 0.94375
    step 19800, training accuracy 0.98
    test accuracy 0.941875
    step 19900, training accuracy 1
    test accuracy 0.943125
    step 20000, training accuracy 0.98
    test accuracy 0.944688
    step 20100, training accuracy 0.98
    test accuracy 0.945312
    step 20200, training accuracy 1
    test accuracy 0.941875
    step 20300, training accuracy 0.96
    test accuracy 0.943438
    step 20400, training accuracy 1
    test accuracy 0.94375
    step 20500, training accuracy 1
    test accuracy 0.944375
    step 20600, training accuracy 1
    test accuracy 0.94625
    step 20700, training accuracy 0.98
    test accuracy 0.945625
    step 20800, training accuracy 1
    test accuracy 0.943437
    step 20900, training accuracy 1
    test accuracy 0.946562
    step 21000, training accuracy 1
    test accuracy 0.946562
    step 21100, training accuracy 0.98
    test accuracy 0.941875
    step 21200, training accuracy 1
    test accuracy 0.945312
    step 21300, training accuracy 0.96
    test accuracy 0.945
    step 21400, training accuracy 0.96
    test accuracy 0.944063
    step 21500, training accuracy 0.98
    test accuracy 0.945625
    step 21600, training accuracy 1
    test accuracy 0.946562
    step 21700, training accuracy 1
    test accuracy 0.94375
    step 21800, training accuracy 0.98
    test accuracy 0.941563
    step 21900, training accuracy 0.98
    test accuracy 0.942188
    step 22000, training accuracy 1
    test accuracy 0.941875
    step 22100, training accuracy 1
    test accuracy 0.947812
    step 22200, training accuracy 0.96
    test accuracy 0.946562
    step 22300, training accuracy 0.98
    test accuracy 0.942188
    step 22400, training accuracy 1
    test accuracy 0.94375
    step 22500, training accuracy 0.98
    test accuracy 0.946562
    step 22600, training accuracy 1
    test accuracy 0.944375
    step 22700, training accuracy 1
    test accuracy 0.943437
    step 22800, training accuracy 1
    test accuracy 0.946562
    step 22900, training accuracy 1
    test accuracy 0.944687
    step 23000, training accuracy 1
    test accuracy 0.944688
    step 23100, training accuracy 0.98
    test accuracy 0.945625
    step 23200, training accuracy 1
    test accuracy 0.946875
    step 23300, training accuracy 1
    test accuracy 0.945625
    step 23400, training accuracy 1
    test accuracy 0.944063
    step 23500, training accuracy 0.96
    test accuracy 0.945312
    step 23600, training accuracy 0.96
    test accuracy 0.948438
    step 23700, training accuracy 1
    test accuracy 0.943438
    step 23800, training accuracy 1
    test accuracy 0.945312
    step 23900, training accuracy 1
    test accuracy 0.945
    step 24000, training accuracy 0.98
    test accuracy 0.944063
    step 24100, training accuracy 1
    test accuracy 0.947187
    step 24200, training accuracy 0.96
    test accuracy 0.94625
    step 24300, training accuracy 1
    test accuracy 0.944063
    step 24400, training accuracy 1
    test accuracy 0.945312
    step 24500, training accuracy 1
    test accuracy 0.946562
    step 24600, training accuracy 0.98
    test accuracy 0.945625
    step 24700, training accuracy 1
    test accuracy 0.945312
    step 24800, training accuracy 1
    test accuracy 0.9425
    step 24900, training accuracy 1
    test accuracy 0.94625
    step 25000, training accuracy 0.98
    test accuracy 0.944375
    step 25100, training accuracy 1
    test accuracy 0.943438
    step 25200, training accuracy 1
    test accuracy 0.945
    step 25300, training accuracy 0.98
    test accuracy 0.945
    step 25400, training accuracy 1
    test accuracy 0.945312
    step 25500, training accuracy 1
    test accuracy 0.943125
    step 25600, training accuracy 1
    test accuracy 0.94625
    step 25700, training accuracy 0.96
    test accuracy 0.94625
    step 25800, training accuracy 0.98
    test accuracy 0.94375
    step 25900, training accuracy 1
    test accuracy 0.945312
    step 26000, training accuracy 1
    test accuracy 0.945625
    step 26100, training accuracy 0.96
    test accuracy 0.945
    step 26200, training accuracy 1
    test accuracy 0.945312
    step 26300, training accuracy 1
    test accuracy 0.946875
    step 26400, training accuracy 0.98
    test accuracy 0.945625
    step 26500, training accuracy 1
    test accuracy 0.944688
    step 26600, training accuracy 1
    test accuracy 0.94375
    step 26700, training accuracy 1
    test accuracy 0.943438
    step 26800, training accuracy 1
    test accuracy 0.947812
    step 26900, training accuracy 1
    test accuracy 0.945
    step 27000, training accuracy 0.98
    test accuracy 0.945
    step 27100, training accuracy 1
    test accuracy 0.944688
    step 27200, training accuracy 1
    test accuracy 0.94125
    step 27300, training accuracy 1
    test accuracy 0.945938
    step 27400, training accuracy 0.96
    test accuracy 0.948125
    step 27500, training accuracy 1
    test accuracy 0.945625
    step 27600, training accuracy 1
    test accuracy 0.945625
    step 27700, training accuracy 1
    test accuracy 0.947812
    step 27800, training accuracy 0.98
    test accuracy 0.944063
    step 27900, training accuracy 1
    test accuracy 0.943125
    step 28000, training accuracy 1
    test accuracy 0.945312
    step 28100, training accuracy 0.96
    test accuracy 0.94625
    step 28200, training accuracy 1
    test accuracy 0.94625
    step 28300, training accuracy 1
    test accuracy 0.945625
    step 28400, training accuracy 1
    test accuracy 0.946562
    step 28500, training accuracy 0.98
    test accuracy 0.945
    step 28600, training accuracy 1
    test accuracy 0.9425
    step 28700, training accuracy 1
    test accuracy 0.94625
    step 28800, training accuracy 1
    test accuracy 0.949062
    step 28900, training accuracy 0.98
    test accuracy 0.944687
    step 29000, training accuracy 1
    test accuracy 0.94375
    step 29100, training accuracy 1
    test accuracy 0.944375
    step 29200, training accuracy 1
    test accuracy 0.948437
    step 29300, training accuracy 1
    test accuracy 0.946562
    step 29400, training accuracy 1
    test accuracy 0.945312
    step 29500, training accuracy 1
    test accuracy 0.94625
    step 29600, training accuracy 0.98
    test accuracy 0.946562
    step 29700, training accuracy 0.98
    test accuracy 0.948438
    step 29800, training accuracy 1
    test accuracy 0.94875
    step 29900, training accuracy 1
    test accuracy 0.949687
    step 30000, training accuracy 0.98
    test accuracy 0.944375
    step 30100, training accuracy 1
    test accuracy 0.9475
    step 30200, training accuracy 1
    test accuracy 0.948125
    step 30300, training accuracy 0.98
    test accuracy 0.947187
    step 30400, training accuracy 1
    test accuracy 0.9475
    step 30500, training accuracy 1
    test accuracy 0.946875
    step 30600, training accuracy 1
    test accuracy 0.94625
    step 30700, training accuracy 1
    test accuracy 0.947188
    step 30800, training accuracy 1
    test accuracy 0.946875
    step 30900, training accuracy 1
    test accuracy 0.945938
    step 31000, training accuracy 1
    test accuracy 0.949062
    step 31100, training accuracy 1
    test accuracy 0.94625
    step 31200, training accuracy 1
    test accuracy 0.946562
    step 31300, training accuracy 1
    test accuracy 0.948437
    step 31400, training accuracy 1
    test accuracy 0.9475
    step 31500, training accuracy 1
    test accuracy 0.948438
    step 31600, training accuracy 1
    test accuracy 0.946562
    step 31700, training accuracy 1
    test accuracy 0.94875
    step 31800, training accuracy 1
    test accuracy 0.949062
    step 31900, training accuracy 1
    test accuracy 0.944687
    step 32000, training accuracy 1
    test accuracy 0.946875
    step 32100, training accuracy 1
    test accuracy 0.94625
    step 32200, training accuracy 1
    test accuracy 0.94625
    step 32300, training accuracy 1
    test accuracy 0.948125
    step 32400, training accuracy 1
    test accuracy 0.947812
    step 32500, training accuracy 1
    test accuracy 0.94875
    step 32600, training accuracy 1
    test accuracy 0.947188
    step 32700, training accuracy 1
    test accuracy 0.948125
    step 32800, training accuracy 0.98
    test accuracy 0.948125
    step 32900, training accuracy 1
    test accuracy 0.946562
    step 33000, training accuracy 1
    test accuracy 0.949687
    step 33100, training accuracy 1
    test accuracy 0.94625
    step 33200, training accuracy 1
    test accuracy 0.948437
    step 33300, training accuracy 1
    test accuracy 0.945312
    step 33400, training accuracy 1
    test accuracy 0.948438
    step 33500, training accuracy 0.98
    test accuracy 0.948438
    step 33600, training accuracy 0.98
    test accuracy 0.946875
    step 33700, training accuracy 1
    test accuracy 0.94875
    step 33800, training accuracy 1
    test accuracy 0.948125
    step 33900, training accuracy 0.98
    test accuracy 0.9475
    step 34000, training accuracy 1
    test accuracy 0.947187
    step 34100, training accuracy 1
    test accuracy 0.948125
    step 34200, training accuracy 1
    test accuracy 0.9475
    step 34300, training accuracy 1
    test accuracy 0.948437
    step 34400, training accuracy 1
    test accuracy 0.94875
    step 34500, training accuracy 1
    test accuracy 0.949375
    step 34600, training accuracy 1
    test accuracy 0.950312
    step 34700, training accuracy 1
    test accuracy 0.948125
    step 34800, training accuracy 1
    test accuracy 0.947188
    step 34900, training accuracy 1
    test accuracy 0.949688
    step 35000, training accuracy 1
    test accuracy 0.945312
    step 35100, training accuracy 1
    test accuracy 0.948438
    step 35200, training accuracy 1
    test accuracy 0.949375
    step 35300, training accuracy 0.98
    test accuracy 0.948437
    step 35400, training accuracy 1
    test accuracy 0.949062
    step 35500, training accuracy 1
    test accuracy 0.945312
    step 35600, training accuracy 1
    test accuracy 0.948438
    step 35700, training accuracy 1
    test accuracy 0.948438
    step 35800, training accuracy 1
    test accuracy 0.949375
    step 35900, training accuracy 1
    test accuracy 0.947812
    step 36000, training accuracy 1
    test accuracy 0.950312
    step 36100, training accuracy 1
    test accuracy 0.948438
    step 36200, training accuracy 1
    test accuracy 0.949375
    step 36300, training accuracy 1
    test accuracy 0.950625
    step 36400, training accuracy 1
    test accuracy 0.94875
    step 36500, training accuracy 1
    test accuracy 0.946562
    step 36600, training accuracy 1
    test accuracy 0.949687
    step 36700, training accuracy 0.98
    test accuracy 0.949688
    step 36800, training accuracy 1
    test accuracy 0.948125
    step 36900, training accuracy 1
    test accuracy 0.951562
    step 37000, training accuracy 1
    test accuracy 0.948438
    step 37100, training accuracy 1
    test accuracy 0.950312
    step 37200, training accuracy 1
    test accuracy 0.95125
    step 37300, training accuracy 1
    test accuracy 0.95
    step 37400, training accuracy 1
    test accuracy 0.94875
    step 37500, training accuracy 0.98
    test accuracy 0.949375
    step 37600, training accuracy 1
    test accuracy 0.950625
    step 37700, training accuracy 1
    test accuracy 0.94875
    step 37800, training accuracy 1
    test accuracy 0.947187
    step 37900, training accuracy 1
    test accuracy 0.951875
    step 38000, training accuracy 1
    test accuracy 0.94875
    step 38100, training accuracy 1
    test accuracy 0.946875
    step 38200, training accuracy 1
    test accuracy 0.947187
    step 38300, training accuracy 1
    test accuracy 0.949062
    step 38400, training accuracy 1
    test accuracy 0.951562
    step 38500, training accuracy 1
    test accuracy 0.947187
    step 38600, training accuracy 1
    test accuracy 0.949688
    step 38700, training accuracy 1
    test accuracy 0.95
    step 38800, training accuracy 1
    test accuracy 0.951875
    step 38900, training accuracy 1
    test accuracy 0.9475
    step 39000, training accuracy 1
    test accuracy 0.95
    step 39100, training accuracy 0.98
    test accuracy 0.949688
    step 39200, training accuracy 1
    test accuracy 0.947188
    step 39300, training accuracy 1
    test accuracy 0.952188
    step 39400, training accuracy 1
    test accuracy 0.949375
    step 39500, training accuracy 1
    test accuracy 0.949375
    step 39600, training accuracy 1
    test accuracy 0.949375
    step 39700, training accuracy 1
    test accuracy 0.947812
    step 39800, training accuracy 0.98
    test accuracy 0.948438
    step 39900, training accuracy 1
    test accuracy 0.95
    step 40000, training accuracy 1
    test accuracy 0.946562
    test accuracy 0.946562



```python
print(offset)
```


```python

```
