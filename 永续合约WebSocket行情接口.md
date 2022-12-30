Bingx官方API文档
==================================================
Bingx开发者文档([English Docs](./Perpetual_Swap_WebSocket_Market_Interface.md))。

<!-- TOC -->

- [Websocket 介绍](#Websocket-介绍)
    - [接入方式](#接入方式)
    - [数据压缩](#数据压缩)
    - [心跳信息](#心跳信息)
    - [订阅方式](#订阅方式)
    - [取消订阅](#取消订阅)
- [Websocket 行情推送](#Websocket-行情推送)
    - [订阅合约交易深度](#1-订阅合约交易深度)
    - [订单最新成交记录](#2-订单最新成交记录)
    - [订阅合约k线数据](#3-订阅合约k线数据)

<!-- /TOC -->

# Websocket 介绍

## 接入方式

行情Websocket的接入URL：`wss://open-api-swap.bingx.com/swap-market`

## 数据压缩

WebSocket 行情接口返回的所有数据都进行了 GZIP 压缩，需要 client 在收到数据之后解压。

## 心跳信息

当用户的Websocket客户端连接到Bingx Websocket服务器后，服务器会定期（当前设为5秒）向其发送心跳字符串Ping，

当用户的Websocket客户端接收到此心跳消息后，应返回字符串Pong消息

## 订阅方式

成功建立与Websocket服务器的连接后，Websocket客户端发送如下请求以订阅特定主题：

{
  "id": "id1",
  "reqType": "sub",
  "dataType": "data to sub",
}

成功订阅后，Websocket客户端将收到确认：

{
  "id": "id1",
  "code": 0,
  "msg": "",
}
之后, 一旦所订阅的数据有更新，Websocket客户端将收到服务器推送的更新消息

## 取消订阅
取消订阅的格式如下：

{
  "id": "id1",
  "reqType": "unsub",
  "dataType": "data to unsub",
}

取消订阅成功确认：

{
  "id": "id1",
  "code": 0,
  "msg": "",
}


# Websocket 行情推送

## 1. 有限档深度信息

    每秒推送有限档深度信息。


**订阅类型**

    dataType 为 <symbol>@depth<level>，比如BTC-USDT@depth5, BTC-USDT@depth20, BTC-USDT@depth100

**订阅参数**  


| 参数名 | 参数类型  | 必填 | 描述 |
| ------------- |----|----|----|
| symbol | String | 是 | 合约名称中需有"-"，如BTC-USDT |
| level | String | 是 | 档数, 如 5，10，20，50，100 |

**备注**

"level" 深度档数定义
| 参数名 | 描述 |
| ----- |----|
| 5 | 5档 |
| 10 | 10档 |
| 20 | 20档 |
| 50 | 50档 |
| 100 | 100档 |

**推送数据** 

| 返回字段|字段说明|  
| ------------- |----|
| code   | 是否有错误信息，0为正常，1为有错误 |
| dataType | 订阅的数据类型，例如 BTC-USDT@depth |
| data | 推送内容 |
| asks   | 卖方深度 |
| bids   | 买方深度 |
| p      | price价格  |
| v      | volume数量 | 

```javascript
    # Response
    {
        "code": 0,
        "dataType": "BTC-USDT@depth",
        "data": {
            "asks": [
                [
                    "5319.94", //变动的价格档位
                    "0.05483456" //数量
                ],[
                    "5319.94", //变动的价格档位
                    "0.05483456" //数量
                ],[
                    "5320.39",//变动的价格档位
                    "1.16307999"//数量
                ],
            ],
            "bids": [
                [
                    "5319.94",//变动的价格档位
                    "0.05483456"//数量
                ],
            ],
        }
    }
```


## 2. 订单最新成交记录

    逐笔交易推送每一笔成交的信息。成交，或者说交易的定义是仅有一个吃单者与一个挂单者相互交易

**订阅类型**

    dataType 为 <symbol>@trade，比如BTC-USDT@trade ETH-USDT@trade

**订阅例子**

    {"id":"24dd0e35-56a4-4f7a-af8a-394c7060909c","dataType":"BTC-USDT@trade"}

**订阅参数**

| 参数名  | 参数类型  | 必填 | 字段描述 | 描述 |
| -------|--------|--- |-------|------|
| symbol | String | 是 |合约名称| 合约名称中需有"-"，如BTC-USDT |

**推送数据** 

| 返回字段| 字段说明                      |  
| ------------ |---------------------------|
| code  | 是否有错误信息，0为正常，1为有错误        |
| dataType | 订阅的数据类型，例如 BTC-USDT@trade |
| data | 推送内容                      |
| T     | 成交时间                      |
| s      | 交易对                       |
| m | 买方是否是做市方。如true，则此次成交是一个主动卖出单，否则是一个主动买入单。   |
| p    | 成交价格                      |
| q   | 成交数量                      |

   ```javascript
    # Response
    {
        "code": 0,
        "dataType": "BTC-USDT@trade",
        "data": {
            "T": 1649832413512,//成交时间,单位毫秒
            "m": true,
            "p": "0.279563",
            "q": "100",
            "s": "BTC-USDT"
        }
    }
   ```

## 3. 订阅合约k线数据

    K线stream逐秒推送所请求的K线种类(最新一根K线)的更新。

**订阅类型**

    dataType 为 <symbol>@kline_<interval>，比如BTC-USDT@kline_1m

**订阅例子**

    {"id":"e745cd6d-d0f6-4a70-8d5a-043e4c741b40","dataType":"BTC-USDT@kline_1m"}

**订阅参数**

| 参数名  | 参数类型  | 必填 | 字段描述 | 描述 |
| -------|--------|--- |-------|------|
| symbol | String | 是 |合约名称| 合约名称中需有"-"，如BTC-USDT |
| interval | String | 是 |k线类型| 参考字段说明，如分钟，小时，周等 |

**备注**

| interval |    字段说明   |
|--------------|-------|
| 1m           | 一分钟K线 |
| 3m           | 三分钟K线 |
| 5m           | 五分钟K线 |
| 15m          | 十五分钟K线 |
| 30m          | 三十分钟K线 |
| 1h           | 一小时K线 |
| 2h           | 两小时K线 |
| 4h           | 四小时K线 |
| 6h           | 六小时K线 |
| 8h           | 八小时K线 |
| 12h          | 十二小时K线 |
| 1d           | 1日K线  |
| 3d           | 3日K线  |
| 1w           | 周K线   |
| 1M           | 月K线   |

**推送数据** 

| 返回字段     | 字段说明               |  
|----------|--------------------|
| code     | 是否有错误信息，0为正常，1为有错误 |
| data     | 推送内容               |
| dataType | 数据类型               |
| c        | 收盘价                |
| h        | 最高价                |
| l        | 最低价                |
| o        | 收盘价                |
| v        | 成交量                |
| s        | 交易对                |

   ```javascript
    # Response
    {
        "code": 0,
        "data": {
            "T": 1649832779999,  //k线时间
            "c": "54564.31", 
            "h": "54711.73",
            "l": "54418.27",
            "o": "54577.41",
            "v": "1607.0727000000002"
        },
        "s": "BTC-USDT" //交易对
        "dataType": "BTC-USDT@kline_1m" // 数据类型
    }
   ```

  **备注**

    更多返回错误代码请看首页的错误代码描述
