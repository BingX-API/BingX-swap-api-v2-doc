Official API Documentation for the Bingx Trading Platform- Websocket
==================================================
Bingx Developer Documentation

<!-- TOC -->

- [Official API Documentation for the bingx Trading Platform- Websocket](#official-api-documentation-for-the-bingx-trading-platform--websocket)
- [Introduction](#introduction)
  - [Access](#access)
  - [Data Compression](#data-compression)
  - [Heartbeats](#heartbeats)
  - [Subscriptions](#subscriptions)
  - [Unsubscribe](#unsubscribe)
- [Perpetual Swap Websocket Market Data](#perpetual-swap-websocket-market-data)
  - [1. Subscribe Market Depth Data](#1-subscribe-market-depth-data)
  - [2. Subscribe the Latest Trade Detail](#2-subscribe-the-latest-trade-detail)
  - [3. Subscribe K-Line Data](#3-subscribe-k-line-data)

<!-- /TOC -->

# Introduction

## Access

the base URL of Websocket Market Data ：`wss://open-api-swap.bingx.com/swap-market`

## Data Compression

All response data from Websocket server are compressed into GZIP format. Clients have to decompress them for further use.

## Heartbeats

Once the Websocket Client and Websocket Server get connected, the server will send a heartbeat- Ping message every 5 seconds (the frequency might change).

When the Websocket Client receives this heartbeat message, it should return Pong message.

## Subscriptions

After successfully establishing a connection with the Websocket server, the Websocket client sends the following request to subscribe to a specific topic:

{
  "id": "id1",
  "reqType": "sub",
  "dataType": "data to sub",
}

After a successful subscription, the Websocket client will receive a confirmation message:

{
  "id": "id1",
  "code": 0,
  "msg": "",
}
After that, once the subscribed data is updated, the Websocket client will receive the update message pushed by the server.

## Unsubscribe
The format of unsubscription is as follows:

{
  "id": "id1",
  "reqType": "unsub",
  "dataType": "data to unsub",
}

Confirmation of Unsubscription:

{
  "id": "id1",
  "code": 0,
  "msg": "",
}


# Perpetual Swap Websocket Market Data

## 1. Subscribe Market Depth Data

    Push limited file depth information every second.

**Subscription Type**

    The dataType is <symbol>@depth<level> E.g. BTC-USDT@depth5

**Subscription Parameters**  

| Parameters | Type | Required | Description                                                          |
| ------------- |----|----|----------------------------------------------------------------------|
| symbol | String | YES | There must be a hyphen/ "-" in the trading pair symbol. eg: BTC-USDT |
| level | String | YES | Depth level, such as 5,10,20,50,100                                  |

**Push Data** 

| Return Parameters | Description |
| ------------- |----|
| code   | With regards to error messages, 0 means normal, and 1 means error |
| dataType | The type of subscribed data, such as market.depth.BTC-USDT.step0.level5 |
| data | Push Data |
| asks   | Sell side depth |
| bids   | Buy side depth |
| p | price |
| v | volume |
```javascript
    # Response
    {
        "code": 0,
        "dataType": "BTC-USDT@depth",
        "data": {
            "asks": [{
                    "p": 5319.94,
                    "v": 0.05483456
                },{
                    "p": 5320.19,
                    "v": 1.05734545
                },{
                    "p": 5320.39,
                    "v": 1.16307999
                },{
                    "p": 5319.94,
                    "v": 0.05483456
                },{
                    "p": 5320.19,
                    "v": 1.05734545
                },{
                    "p": 5320.39,
                    "v": 1.16307999
                },
            ],
            "bids": [{
                    "p": 5319.94,
                    "v": 0.05483456
                },{
                    "p": 5320.19,
                    "v": 1.05734545
                },{
                    "p": 5320.39,
                    "v": 1.16307999
                },{
                    "p": 5319.94,
                    "v": 0.05483456
                },{
                    "p": 5320.19,
                    "v": 1.05734545
                },{
                    "p": 5320.39,
                    "v": 1.16307999
                },
            ],
        }
    }
```


## 2. Subscribe the Latest Trade Detail

    Subscribe to the trade detail data of a trading pair

**Subscription Type**

    The dataType is <symbol>@trade 
    E.g. BTC-USDT@trade

**Subscription Example**

    {"id":"24dd0e35-56a4-4f7a-af8a-394c7060909c","dataType":"BTC-USDT@trade"}

**Subscription Parameters**

| Parameters | Type | Required | Field description | Description |
| -------|--------|--- |-------|------|
| symbol | String | YES | Trading pair symbol | There must be a hyphen/ "-" in the trading pair symbol. eg: BTC-USDT |

**Push Data**

| Return Parameters | Description |
|-------------------|----|
| code              | With regards to error messages, 0 means normal, and 1 means error |
| dataType          | The type of data subscribed, such as BTC-USDT@trade |
| data              | Push Data |
| T                 |transaction time |
| s                 | trading pair |
| m                 |Whether the buyer is a market maker. If true, this transaction is an active sell order, otherwise it is an active buy order. |
| p                 | deal price |
| q                 | The number of transactions |

   ```javascript
    # Response
{
  "code": 0,
  "dataType": "BTC-USDT@trade",
  "data": {
    "T": 1649832413512,//Transaction time, in milliseconds
    "m": true,
    "p": "0.279563",
    "q": "100",
    "s": "BTC-USDT"
  }
}
   ```

## 3. Subscribe K-Line Data

    Subscribe to market k-line data of one trading pair

**Subscription Type**

    The dataType is <symbol>@kline_<interval>
    E.g. BTC-USDT@kline_1m

**Subscription Example**

    {"id":"e745cd6d-d0f6-4a70-8d5a-043e4c741b40","dataType":"BTC-USDT@kline_1m"}

**Subscription Parameters**

| Parameters | Type   | Required | Field Description   | Description                                                  |
| ---------- | ------ | -------- | ------------------- | ------------------------------------------------------------ |
| symbol     | String | YES      | Trading pair symbol | There must be a hyphen/ "-" in the trading pair symbol. eg: BTC-USDT |
| interval  | String | YES      | K-Line Type         | The type of K-Line ( minutes, hours, weeks etc.)             |

**Remarks**

| interval | Field Description |
|----------|-------------------|
| 1m       | 1 min Kline       |
| 3m       | 3 min Kline       |
| 5m       | 5 min Kline       |
| 15m      | 15 min Kline      |
| 30m      | 30 min Kline      |
| 1h       | 1-hour Kline      |
| 2h       | 2-hour Kline      |
| 4h       | 4-hour Kline      |
| 6h       | 6-hour Kline      |
| 8h       | 8-hour Kline      |
| 12h      | 12-hour Kline     |
| 1d       | 1-Day Kline       |
| 3d       | 3-Day Kline       |
| 1w       | 1-Week Kline      |
| 1M       | 1-Month Kline     |

**Push Data**

| Return Parameters | Field Description                                                 |
|-------------------|-------------------------------------------------------------------|
| code              | With regards to error messages, 0 means normal, and 1 means error |
| data              | Push Data                                                         |
| dataType          | Data Type                                                         |
| T                 | time,Unit: ms                                                             |
| c                 | Closing Price                                                     |
| h                 | High  Price                                                       |
| l                 | Low   Price                                                       |
| o                 | Opening Price                                                     |
| v                 | volume                                                            |
| s                 | trading pair                                  |

```javascript
 # Response
{
  "code": 0,
  "data": {
    "T": 1649832779999,  //k line time
    "c": "54564.31",
    "h": "54711.73",
    "l": "54418.27",
    "o": "54577.41",
    "v": "1607.0727000000002"
  },
  "s": "BTC-USDT" //trading pair
  "dataType": "BTC-USDT@kline_1m"
}
```

**Remarks**

    For more about return error codes, please see the error code description on the homepage.
