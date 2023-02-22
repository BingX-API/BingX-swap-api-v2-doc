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
- [Websocket account information push](#Websocket-account-information-push)
  - [listenKey expired push](#1-listenKey-expired-push)
  - [Account Balance and Position Update Push](#2-Account-Balance-and-Position-Update-Push)
  - [Order Update Push](#3-Order-update-push)
  - [Configuration updates such as leverage and margin mode](#4-account-configuration-updates)

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

# Websocket account information push

Note that obtaining such information requires websocket authentication, use listenKey, and check the [Rest interface document](https://github.com/BingX-API/BingX-swap-api-v2-doc/blob/main/Perpetual_Swap_API_Documentation.md#other-interface)

The websocket interface is `wss://open-api-swap.bingx.com/swap-market`

The stream name of the subscription account data stream is `/swap-market?listenKey=`
```
wss://open-api-swap.bingx.com/swap-market?listenKey=a8ea75681542e66f1a50a1616dd06ed77dab61baa0c296bca03a9b13ee5f2dd7
```

## 1. listenKey expired push
The user data stream will push this event when the valid listenKey used by the current connection expires.

Notice:

- This event is not necessarily related to the interruption of the websocket connection
- This message will only be received when the valid listenKey being connected has expired
- After receiving this message, the user data stream will not be updated until the user uses a new and valid listenKey

**Push data**

```
{
    "e":"listenKeyExpired", // event type
    "E":1676964520421, // event time
    "listenKey":"53c1067059c5401e216ec0562f4e9741f49c3c18239a743653d844a50c4db6c0" // invalid listenKey
}
```

## 2. Account balance and position update push

The event type of the account update event is fixed as ACCOUNT_UPDATE

- When the account information changes, this event will be pushed:

  - This event will only be pushed when there is a change in account information (including changes in funds, positions, etc.);
    This event will not be pushed if the change in the order status does not cause changes in the account and positions;
  - position information: push only when there is a change in the symbol position.

- Fund balance changes caused by "FUNDING FEE", only push brief events:

  - When "FUNDING FEE" occurs in a user's cross position, the event ACCOUNT_UPDATE will only push the relevant user's asset balance information B (only push the asset balance information related to the occurrence of FUNDING FEE), and will not push any position information P.
  - When "FUNDING FEE" occurs in a user's isolated position, the event ACCOUNT_UPDATE will only push the relevant user asset balance information B (only push the asset balance information used by "FUNDING FEE"), and related position information P( Only the position information where this "FUNDING FEE" occurred is pushed), and the rest of the position information will not be pushed.
- The field "m" represents the reason for the launch of the event, including the following possible types:
  -DEPOSIT
  - WITHDRAW
    -ORDER
  - FUNDING_FEE

**Push data**

```
{
    "e": "ACCOUNT_UPDATE", //event type
    "E":1676603102163, //event time
    "T":1676603102163,
    "a":{ // account update event
        "m":"ORDER", // event launch reason
        "B":[ // balance information
            {
                "a":"USDT", // asset name
                "wb":"5277.59264687", // wallet balance
                "cw":"5233.21709203", // Wallet balance excluding isolated margin
                "bc":"0" // wallet balance change amount
            }
        ],
        "P":[
            {
                "s":"LINK-USDT", // trading pair
                "pa":"108.84300000", // position
                "ep":"7.25620000", // entry price
                "up": "1.42220000", // unrealized profit and loss of positions
                "mt":"isolated", // margin mode
                "iw": "23.19081642", // If it is an isolated position, the position margin
                "ps":"SHORT" // position direction
            }
        ]
    }
}
```

## 3. Order update push

This type of event will be pushed when a new order is created, an order has a new deal, or a new status change. The event type is unified as ORDER_TRADE_UPDATE

order direction
- BUY buy
- SELL sell


Order Type
- MARKET market order
- LIMIT limit order
- STOP stop loss order
- TAKE_PROFIT take profit order
- LIQUIDATION strong liquidation order

The specific execution type of this event
- NEW
- CANCELED removed
- CALCULATED order ADL or liquidation
- EXPIRED order lapsed
- TRADE transaction

Order Status
- NEW
- PARTIALLY_FILLED
- FILLED
- CANCELED
- EXPIRED

**Push data**

```
{
    "e":"ORDER_TRADE_UPDATE", // event type
    "E":1676973375161, // event time
    "o": { //
        "s":"LINK-USDT", // trading pair
        "c":"", // client custom order ID
        "i":1627970445070303232, // Order ID
        "S":"SELL", // order direction
        "o":"MARKET", // order type
        "q":"5.00000000", // order quantity
        "p":"7.82700000", // order price
        "ap":"7.82690000", // order average price
        "x":"TRADE", // The specific execution type of this event
        "X":"FILLED", // current status of the order
        "N":"USDT", // Fee asset type
        "n":"-0.01369708", // handling fee
        "T":1676973375149, // transaction time
        "wt": "MARK_PRICE", // trigger price type: MARK_PRICE mark price, CONTRACT_PRICE latest price, INDEX_PRICE index price
        "ps":"SHORT", // Position direction: LONG or SHORT
        "rp":"0.00000000" // The transaction achieves profit and loss
    }
}
```

## 4. Configuration updates such as leverage and margin mode
When the account configuration changes, the event type will be pushed as ACCOUNT_CONFIG_UPDATE

When the leverage of a trading pair changes, the push message will contain the object ac, which represents the account configuration of the trading pair, where s represents the specific trading pair, l represents the leverage of long positions, S represents the leverage of short positions, and mt represents the margin mode.

**Push data**

```
{
    "e":"ACCOUNT_CONFIG_UPDATE", // event type
    "E":1676878489992, // event time
    "ac":{
        "s": "BTC-USDT", // trading pair
        "l":12, // long position leverage
        "S":9, // long position leverage
        "mt":"cross" // margin mode
    }
}
```

**Remarks**

    For more about return error codes, please see the error code description on the homepage.
