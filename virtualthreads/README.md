# Cost of platform thread

![Cost of Platform Thread.png](img/img.png)

---

### Oha performance results for 100 request with 50 concurrent user 

| Client                              | Virtual Thread | Total Time | Slowest | Fastest | Average | Request/Sec |
|-------------------------------------|----------------|------------|---------|---------|---------|-------------|
| [Rest Client](#Rest-Client)         | Disable        | 20.6840    | 20.6582 | 2.0417  | 8.2637  | 4.8347      |
| Reactive                            | Disable        | 4.3121     | 2.2667  | 2.0078  | 2.0921  | 23.1908     |
| Reactive Block                      | Disable        | 20.3829    | 18.3009 | 2.0108  | 8.1354  | 4.9601      |
| [Rest Client](#Rest-Client-Virtual) | Enable         | 4.2960     | 2.2717  | 2.0102  | 2.1096  | 23.2776     |
| Reactive                            | Enable         | 4.3511     | 2.3287  | 2.0070  | 2.1027  | 22.9827     |
| Reactive Block                      | Enable         | 4.2335     | 2.1829  | 2.0390  | 2.0916  | 23.6214     |
| Multi Rest Client                   | Disable        | 40.7483    | 36.6893 | 4.0302  | 16.3009 | 2.4541      |
| Multi Reactive                      | Disable        | 4.3761     | 2.3061  | 2.0440  | 2.1301  | 22.8516     |
| Multi Reactive Block                | Disable        | 20.3194    | 14.1286 | 2.0090  | 8.1074  | 4.9214      |
| Multi Rest Client                   | Enable         | 8.3552     | 4.2744  | 4.0059  | 4.1265  | 11.9687     |
| Multi Reactive                      | Enable         | 4.4000     | 2.3336  | 2.0298  | 2.1362  | 22.7274     |
| Multi Reactive Block                | Enable         | 4.3289     | 2.2420  | 2.0069  | 2.1011  | 23.1004     |

---

# Performance without virtual threads

## Rest Client

![img_1.png](img/img_1.png)

## Reactive

![img_2.png](img/img_2.png)

## Reactive with block

![img_3.png](img/img_3.png)

## Multiple calls with Rest Client

![img_4.png](img/img_4.png)

## Multiple calls with Reactive

![img_5.png](img/img_5.png)

## Multiple calls with Reactive block

![img_6.png](img/img_6.png)

---

# Performance with virtual threads

## Rest Client Virtual

![img_7.png](img/img_7.png)

## Reactive Virtual

![img_8.png](img/img_8.png)

## Reactive with block Virtual

![img_9.png](img/img_9.png)

## Multiple calls with Rest Client Virtual

![img_10.png](img/img_10.png)


## Multiple calls with Reactive Virtual

![img_11.png](img/img_11.png)

## Multiple calls with Reactive block Virtual

![img_12.png](img/img_12.png)
