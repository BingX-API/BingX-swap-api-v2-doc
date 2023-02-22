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
- [Websocket 账户信息推送](#Websocket-账户信息推送)
  - [listenKey过期推送](#1-listenKey过期推送)
  - [账户余额和仓位更新推送](#2-账户余额和仓位更新推送)
  - [订单更新推送](#3-订单更新推送)
  - [杠杆倍数和保证金模式等配置更新推送](#4-账户配置更新推送)

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

# Websocket 账户信息推送

注意需要获取此类信息需要 websocket 鉴权，使用 listenKey，详细方式查看 [Rest 接口文档](https://github.com/BingX-API/BingX-swap-api-v2-doc/blob/main/%E4%B8%93%E4%B8%9A%E5%90%88%E7%BA%A6API%E6%8E%A5%E5%8F%A3.md#%E5%85%B6%E4%BB%96%E6%8E%A5%E5%8F%A3)

websocket接口是 `wss://open-api-swap.bingx.com/swap-market`

订阅账户数据流的stream名称为 `/swap-market?listenKey=`
```
wss://open-api-swap.bingx.com/swap-market?listenKey=a8ea75681542e66f1a50a1616dd06ed77dab61baa0c296bca03a9b13ee5f2dd7
```

## 1. listenKey过期推送
当前连接使用的有效listenKey过期时，user data stream 将会推送此事件。

注意:

- 此事件与websocket连接中断没有必然联系
- 只有正在连接中的有效listenKey过期时才会收到此消息
- 收到此消息后user data stream将不再更新，直到用户使用新的有效的listenKey

**推送数据**

```
{
    "e":"listenKeyExpired", // 事件类型
    "E":1676964520421,      // 事件时间
    "listenKey":"53c1067059c5401e216ec0562f4e9741f49c3c18239a743653d844a50c4db6c0" // 失效的listenKey
}
```

## 2. 账户余额和仓位更新推送

账户更新事件的 event type 固定为 ACCOUNT_UPDATE

- 当账户信息有变动时，会推送此事件：

  - 仅当账户信息有变动时(包括资金、仓位等发生变化)，才会推送此事件；
订单状态变化没有引起账户和持仓变化的，不会推送此事件；
  - position 信息：仅当symbol仓位有变动时推送。

- "FUNDING FEE" 引起的资金余额变化，仅推送简略事件：

  - 当用户某全仓持仓发生"FUNDING FEE"时，事件ACCOUNT_UPDATE将只会推送相关的用户资产余额信息B(仅推送FUNDING FEE 发生相关的资产余额信息)，而不会推送任何持仓信息P。
  - 当用户某逐仓仓持仓发生"FUNDING FEE"时，事件ACCOUNT_UPDATE将只会推送相关的用户资产余额信息B(仅推送"FUNDING FEE"所使用的资产余额信息)，和相关的持仓信息P(仅推送这笔"FUNDING FEE"发生所在的持仓信息)，其余持仓信息不会被推送。
- 字段"m"代表了事件推出的原因，包含了以下可能类型:
  - DEPOSIT
  - WITHDRAW
  - ORDER
  - FUNDING_FEE

**推送数据**

```
{
    "e":"ACCOUNT_UPDATE",               //事件类型
    "E":1676603102163,                  //事件时间
    "T":1676603102163,
    "a":{                               // 账户更新事件
        "m":"ORDER",                    // 事件推出原因 
        "B":[                           // 余额信息
            {
                "a":"USDT",             // 资产名称
                "wb":"5277.59264687",   // 钱包余额
                "cw":"5233.21709203",   // 除去逐仓仓位保证金的钱包余额
                "bc":"0"                // 钱包余额改变量
            }
        ],
        "P":[
            {
                "s":"LINK-USDT",        // 交易对
                "pa":"108.84300000",    // 仓位
                "ep":"7.25620000",      // 入仓价格
                "up":"1.42220000",      // 持仓未实现盈亏
                "mt":"isolated",        // 保证金模式
                "iw":"23.19081642",     // 若为逐仓，仓位保证金
                "ps":"SHORT"            // 持仓方向
            }
        ]
    }
}
```

## 3. 订单更新推送

当有新订单创建、订单有新成交或者新的状态变化时会推送此类事件 事件类型统一为 ORDER_TRADE_UPDATE

订单方向
 - BUY 买入
 - SELL 卖出


订单类型
 - MARKET 市价单
 - LIMIT 限价单
 - STOP 止损单
 - TAKE_PROFIT 止盈单
 - LIQUIDATION 强平单

本次事件的具体执行类型
 - NEW
 - CANCELED 已撤
 - CALCULATED 订单ADL或爆仓
 - EXPIRED 订单失效
 - TRADE 交易

订单状态
 - NEW
 - PARTIALLY_FILLED
 - FILLED
 - CANCELED
 - EXPIRED

**推送数据**

```
{
    "e":"ORDER_TRADE_UPDATE",     // 事件类型
    "E":1676973375161,            // 事件时间
    "o":{                         // 
        "s":"LINK-USDT",          // 交易对
        "c":"",                   // 客户端自定订单ID
        "i":1627970445070303232,  // 订单ID
        "S":"SELL",               // 订单方向
        "o":"MARKET",             // 订单类型
        "q":"5.00000000",         // 订单委托数量
        "p":"7.82700000",         // 订单委托价格
        "ap":"7.82690000",        // 订单平均价格
        "x":"TRADE",              // 本次事件的具体执行类型
        "X":"FILLED",             // 订单的当前状态
        "N":"USDT",               // 手续费资产类型
        "n":"-0.01369708",        // 手续费
        "T":1676973375149,        // 成交时间
        "wt":"MARK_PRICE",        // 触发价类型：MARK_PRICE 标记价格，CONTRACT_PRICE 最新价格，INDEX_PRICE 指数价格
        "ps":"SHORT",             // 持仓方向：LONG or SHORT
        "rp":"0.00000000"         // 该交易实现盈亏
    }
}
```

## 4. 杠杆倍数和保证金模式等配置更新推送
当账户配置发生变化时会推送此类事件类型统一为 ACCOUNT_CONFIG_UPDATE

当交易对杠杆倍数发生变化时推送消息体会包含对象ac表示交易对账户配置，其中s代表具体的交易对，l代表多仓杠杆倍数，S代表空仓杠杆倍数，mt代表保证金模式。

**推送数据**

```
{
    "e":"ACCOUNT_CONFIG_UPDATE", // 事件类型
    "E":1676878489992,           // 事件时间
    "ac":{
        "s":"BTC-USDT",          // 交易对
        "l":12,                  // 多仓杠杆倍数
        "S":9,                   // 多仓杠杆倍数
        "mt":"cross"             // 保证金模式
    }
}
```

  **备注**

    更多返回错误代码请看首页的错误代码描述
