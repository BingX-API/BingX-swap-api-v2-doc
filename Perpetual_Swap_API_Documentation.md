Official API Documentation for the Bingx Trading Platform
==================================================
Bingx Developer Documentation

<!-- TOC -->

- [Official API Documentation for the bingx Trading Platform](#official-api-documentation-for-the-bingx-trading-platform)
- [Introduction](#introduction)
- [interface](#interface)
- [**Authentication**](#authentication)
  - [Generate an API Key](#generate-an-api-key)
  - [Permission Settings](#permission-settings)
  - [Make Requests](#make-requests)
  - [Signature](#signature)
  - [Requests](#requests)
- [**Basic Information**](#basic-information)
  - [Common Error Codes](#common-error-codes)
  - [Timestamp](#timestamp)
    - [Example](#example)
  - [Numbers](#numbers)
  - [Rate Limits](#rate-limits)
    - [REST API](#rest-api)
  - [Get Server Time](#get-server-time)
- [Market Interface](#market-interface)
  - [1. Contract Information](#1-contract-information)
  - [2. Get Latest Price of a Trading Pair](#2-get-latest-price-of-a-trading-pair)
  - [3. Get Market Depth](#3-get-market-depth)
  - [4. The latest Trade of a Trading Pair](#4-the-latest-trade-of-a-trading-pair)
  - [5. Current Funding Rate](#5-current-funding-rate)
  - [6. Funding Rate History](#6-funding-rate-history)
  - [7. K-Line Data](#7-k-line-data)
  - [8. Get Swap Open Positions](#9-get-swap-open-positions)
  - [9. Get Ticker](#10-get-ticker)
- [Account Interface](#account-interface)
  - [**1. Get Perpetual Swap Account Asset Information**](#1-get-perpetual-swap-account-asset-information)
  - [**2. Perpetual Swap Positions**](#2-perpetual-swap-positions)
  - [**3. Get Account Profit and Loss Fund Flow**](#3-get-account-profit-and-loss-fund-flow)
- [Trade Interface](#trade-interface)
  - [1. Trade order](#1-trade-order)
  - [2. Bulk order](#2-bulk-order)
  - [3. One-Click Close All Positions](#3-one-click-close-all-positions)
  - [4. Cancel an Order](#4-cancel-an-order)
  - [5. Cancel a Batch of Orders](#5-cancel-a-batch-of-orders)
  - [6. Cancel All Orders](#6-cancel-all-orders)
  - [7. Query pending orders](#7-query-all-current-pending-orders)
  - [8. Query Order](#8-query-order)
  - [9. Query Margin Mode](#9-query-margin-mode)
  - [10. Switch Margin Mode](#10-switch-margin-mode)
  - [11. Query Leverage](#11-query-leverage)
  - [12. Switch Leverage](#12-switch-leverage)
  - [13. User's Force Orders](#13-users-force-orders)
  - [14. User's History Orders](#14-users-history-orders)
  - [15. Adjust isolated margin](#15-adjust-isolated-margin)
- [Other Interface](#other-interface)
  - [generate Listen Key](#generate-listen-key)
  - [extend Listen Key Validity period](#extend-listen-key-validity-period)
  - [delete Listen Key](#delete-listen-key)

<!-- /TOC -->

# Introduction

Welcome to the [Bingx][https://bingx.com] API. You can use our API to access market data, trading, and account management endpoints of Perpetual Swap. The market data API is publicly accessible and provides market data such as The Latest Trade of a Trading Pair. The account and trading APIs require authentication with an API Key which allows you to place and cancel orders and enquire order status and account info.

# interface
- The interface of the GET method, parameters must be sent in the query string.
- Interface for POST, PUT, and DELETE methods, parameters can be sent in query string or request body (content type application/x-www-form-urlencoded). It is allowed to mix these two ways to send parameters. But if the same parameter name exists in both query string and request body, the one in query string will be used first.
- The order of parameters is not required.

# **Authentication**
## Generate an API Key

Before being able to sign any requests, you must create an API Key at the API Management page on [Bingx](https://bingx.com/zh-hk/account/api/). Upon creating a key you will have 2 pieces of information which you should remember:
* API Key
* Secret Key


The API Key and Secret Key will be randomly generated and provided by Bingx.

## Permission Settings
* The default permission for newly created APIs is read-only.
* If you need to perform write operations such as placing an order through the API, you need to modify it to the corresponding permission on the UI.

## Make Requests

All private REST requests must contain the following parameters:
* Pass the API Key with X-BX-APIKEY on the request header.
* The request parameter carries the signature obtained by using the signature algorithm.
* timestamp is the timestamp of your request, in milliseconds. When the server receives the request, it will judge the timestamp in the request. If it is sent before 5000 milliseconds, the request will be considered invalid. This time window value can be defined by sending the optional parameter recvWindow.

## Signature
The signature request parameter is encrypted using the **HMAC SHA256** method.

**Example: Signature for adjusting currency leverage request parameters**
- Interface parameters:
```
symbol=BTC-USDT
timestamp=1667872120843
side=LONG
leverage=6
```
- api information:
```
apiKey = hO6oQotzTE0S5FRYze2Jx2wGx7eVnJGMolpA1nZyehsoMgCcgKNWQHd4QgTFZuwl4Zt4xMe2PqGBegWXO4A
secretKey = mheO6dR8ovSsxZQCOYEFCtelpuxcWGTfHw7te326y6jOwq5WpvFQ9JNljoTwBXZGv5It07m9RXSPpDQEK2w
```
- Parameters sent via `query string` example
```
1. Splicing interface parameters: symbol=BTC-USDT&timestamp=1667872120843&side=LONG&leverage=6
2. Use secretKey to generate a signature for the concatenated parameter string: 4f581ecdb1fa09b9d6e57886b6f70cffed17f82b93399722939e49a38edec2bf
   echo -n "symbol=BTC-USDT&timestamp=1667872120843&side=LONG&leverage=6" | openssl dgst -sha256 -hmac "mheO6dR8ovSsxZQCOYEFCtelpuxcWGTfHw7te326y6jOwq5WpvFQ9JNljoTwBXZGv5It07m9RXSPpDQEK2w" -hex
3.Send request: curl -H 'X-BX-APIKEY: hO6oQotzTE0S5FRYze2Jx2wGx7eVnJGMolpA1nZyehsoMgCcgKNWQHd4QgTFZuwl4Zt4xMe2PqGBegWXO4A' 'https://open-api.bingx.com/openApi/swap/v2/trade/leverage?symbol=BTC-USDT&timestamp=1667872120843&side=LONG&leverage=6&signature=4f581ecdb1fa09b9d6e57886b6f70cffed17f82b93399722939e49a38edec2bf'
```
- Parameters are sent through the `request body` example
```
1. Splicing interface parameters: symbol=BTC-USDT&timestamp=1667872120843&side=LONG&leverage=6
2. Use secretKey to generate a signature for the concatenated parameter string: 4f581ecdb1fa09b9d6e57886b6f70cffed17f82b93399722939e49a38edec2bf
   echo -n "symbol=BTC-USDT&timestamp=1667872120843&side=LONG&leverage=6" | openssl dgst -sha256 -hmac "mheO6dR8ovSsxZQCOYEFCtelpuxcWGTfHw7te326y6jOwq5WpvFQ9JNljoTwBXZGv5It07m9RXSPpDQEK2w" -hex
3. Send request: curl -H 'X-BX-APIKEY: hO6oQotzTE0S5FRYze2Jx2wGx7eVnJGMolpA1nZyehsoMgCcgKNWQHd4QgTFZuwl4Zt4xMe2PqGBegWXO4A' -X POST 'https://open-api.bingx.com/openApi/swap/v2/trade/leverage' -d 'symbol=BTC-USDT&timestamp=1667872120843&side=LONG&leverage=6&signature=4f581ecdb1fa09b9d6e57886b6f70cffed17f82b93399722939e49a38edec2bf'
```
- Parameters sent through `query string` and `request body` example
```
queryString: symbol=BTC-USDT&timestamp=1668159715051
requestBody: side=LONG&leverage=6

1. Splicing interface parameters: symbol=BTC-USDT&timestamp=1668159715051side=LONG&leverage=6
2. Use secretKey to generate a signature for the concatenated parameter string: 8b756b01e7a30f02e19c58a91ab01b29528694316b08a51ecb8dd072942bd47d
   echo -n "symbol=BTC-USDT&timestamp=1668159715051side=LONG&leverage=6" | openssl dgst -sha256 -hmac "mheO6dR8ovSsxZQCOYEFCtelpuxcWGTfHw7te326y6jOwq5WpvFQ9JNljoTwBXZGv5It07m9RXSPpDQEK2w" -hex
3. Send request: curl -H 'X-BX-APIKEY: hO6oQotzTE0S5FRYze2Jx2wGx7eVnJGMolpA1nZyehsoMgCcgKNWQHd4QgTFZuwl4Zt4xMe2PqGBegWXO4A' -X POST 'https://open-api.bingx.com/openApi/swap/v2/trade/leverage?symbol=BTC-USDT&timestamp=1668159715051&signature=8b756b01e7a30f02e19c58a91ab01b29528694316b08a51ecb8dd072942bd47d' -d 'side=LONG&leverage=6'
```

## Requests

Root URL for REST access：`https://open-api.bingx.com`

**Request Description**

1、Request parameter: Parameter encapsulation is performed according to the interface request parameter specification.

2、Submit request parameters: Submit the encapsulated request parameters to the server through POST/GET/DELETE, etc.

3、Server response: The server first performs parameter security verification on the user request data, and returns the response data to the user in JSON format after passing the verification according to the business logic.

4、Data processing: process the server response data.

**Success**

A successful response is indicated by HTTP status code 200 and may optionally contain a body. If the response has a body, it will be included under each resource below.

# **Basic Information**

## Common Error Codes

**Common HTTP Error Codes**

### Types

* 4XX error codes are used to indicate wrong request content, behavior, format.
* 5XX error codes are used to indicate problems with the Bingx service.

### Error Codes

* 400 Bad Request – Invalid request format 
* 401 Unauthorized – Invalid API Key 
* 403 Forbidden – You do not have access to the requested resource
* 404 Not Found 
* 429 Too Many Requests - Return code is used when breaking a request rate limit.
* 418 return code is used when an IP has been auto-banned for continuing to send requests after receiving 429 codes.
* 500 Internal Server Error – We had a problem with our server
* 504 return code means that the API server has submitted a request to the service center but failed to get a response. It should be noted that the 504 return code does not mean that the request failed. It refers to an unknown status. The request may have been executed, or it may have failed. Further confirmation is required.


### Common business error codes:
* 100001 - signature verification failed
* 100500 - Internal system error
* 80001 - request failed
* 80012 - service unavailable
* 80014 - Invalid parameter
* 80016 - Order does not exist
* 80017 - position does not exist

### Notes:

* If it fails, there will be an error description included in the response body.
* Errors may be thrown from every interface.

## Timestamp

Unless otherwise specified, all timestamps from the API are returned with millisseconds resolution.


The timestamp of the request must be within 5 seconds of the API service time, otherwise the request will be considered expired and rejected. If there is a large deviation between the local server time and the API server time, we recommend that you update the http header by querying the API server time.
### Example

1587091154123

## Numbers

Decimal numbers are returned as "Strings" in order to preserve full precision. It is recommended that the numbers are converted to "Strings" to avoid truncation and precision loss.

Integer numbers (such as trade ID and sequences) are unquoted.

## Rate Limits

To prevent abuse, Bingx imposes rate limits on incoming requests. When a rate limit is exceeded, the system will automatically limit the requests.

### REST API

* Market Interface is the public interface. The rate limit of public interfaces is 20 requests every 1 second at most for each IP.
* Account Interface and Transaction Interface are private interfaces. Generally, the private interface rate limit is at most 10 requests every 1 second for each UID.
* The specific rate limits are indicated in the documentation for some endpoints.

## Get Server Time

**HTTP Requests**

  ```http
    GET /openApi/swap/v2/server/time
  ```

**Request Parameters**

    null   

**Return Parameters**

| Field       | Type   | Description                                                  |
| ----------- | ------ | ------------------------------------------------------------ |
| code        | Int64  | error code, 0 means successfully response, others means response failure |
| msg         | String | Error Details Description                                    |
| serverTime | Int64  | The current time of the system，unit: ms                     |

```javascript
{
  "code": 0,
  "msg": "",
  "data": {
    "serverTime": 1672025091160
  }
}
```
# Market Interface

## 1. Contract Information

**HTTP Requests**

```http
    GET /openApi/swap/v2/quote/contracts
```

**parameter**

    none

**response**

| parameter name | type | field description |
|-----------------|---------|:-----------------: |
| contractId | string | contract ID |
| symbol | string | trading pair, for example: BTC-USDT |
| size | string | contract size, such as 0.0001 BTC |
| quantityPrecision | int | transaction quantity precision |
| pricePrecision | int | price precision |
| feeRate | float64 | transaction fee |
| tradeMinLimit | int | The smallest trading unit, the unit is Zhang |
| currency | string | settlement and margin currency asset |
| asset | string | contract trading asset |
| status | int | 0 offline, 1 online |
| maxLongLeverage | int | The maximum leverage for long transactions |
| maxShortLeverage | int | Maximum leverage for short trades |

```json
{
  "code": 0,
  "msg": "",
  "data": [
    {
      "contractId": "100",
      "symbol": "BTC-USDT",
      "size": "0.0001",
      "quantityPrecision": 4,
      "pricePrecision": 1,
      "feeRate": 0.0005,
      "tradeMinLimit": 1,
      "maxLongLeverage": 150,
      "maxShortLeverage": 150,
      "currency": "USDT",
      "asset": "BTC",
      "status": 1
    },
    {
      "contractId": "101",
      "symbol": "ETH-USDT",
      "size": "0.01",
      "quantityPrecision": 2,
      "pricePrecision": 2,
      "feeRate": 0.0005,
      "tradeMinLimit": 1,
      "maxLongLeverage": 125,
      "maxShortLeverage": 125,
      "currency": "USDT",
      "asset": "ETH",
      "status": 1
    }
}
```
## 2. Get Latest Price of a Trading Pair

**HTTP Requests**

  ```http
    GET /openApi/swap/v2/quote/price
  ```

**parameter**

| parameter name | type | required | description |
| -------|--------|----------|------|
| symbol | string | No | Trading pair, for example: BTC-USDT, please use capital letters |

- If no transaction pair parameters are sent, all transaction pair information will be returned

**response**

| parameter name | type | description |
|--------|--------|-------|
| symbol | string | trading pair, for example: BTC-USDT |
| price | string | price |
| time | int64 | matching engine time |

```javascript
{
  "code": 0,
  "msg": "",
  "data": {
    "symbol": "BTC-USDT",
    "price": "16882.0",
    "time": 1672025339378
  }
}
```

## 3. Get Market Depth

**HTTP Requests**

```http
    GET /openApi/swap/v2/quote/depth
```

**parameter**

| parameter name |  type    | required | description                   |
| ------------- |--------|----------|----------------------|
| symbol | string | Yes      |Trading pair, for example: BTC-USDT, please use capital letters |
| limit | int    | No       | Default 20, optional value:[5, 10, 20, 50, 100, 500, 1000]           |

**response**

| parameter name | type | description |
|------|--------|----------------------|
| T | int64 | System time, unit: millisecond |
| asks | array | depth of asks. first element price, second element quantity |
| bids | array | Buyer depth. first element price, second element quantity |

```javascript
{
  "code": 0,
          "msg": "",
          "data": {
    "T": 1672025377603,
            "bids": [
      [
        "16880.50000000",
        "1083739.00000000"
      ],
      [
        "16880.00000000",
        "851709.00000000"
      ],
      [
        "16879.50000000",
        "359692.00000000"
      ],
      [
        "16879.00000000",
        "56341.00000000"
      ],
      [
        "16878.50000000",
        "368408.00000000"
      ]
    ],
            "asks": [
      [
        "16881.00000000",
        "1518457.00000000"
      ],
      [
        "16881.50000000",
        "1.00000000"
      ],
      [
        "16882.00000000",
        "960717.00000000"
      ],
      [
        "16882.50000000",
        "8.00000000"
      ],
      [
        "16883.00000000",
        "948166.00000000"
      ]
    ]
  }
}
```
## 4. The latest Trade of a Trading Pair

**HTTP Requests**

  ```http
    GET /openApi/swap/v2/quote/trades
  ```

**parameter**

| parameter name | type | required | description |
| -------|--------|-------|------|
| symbol | string | yes | trading pair, for example: BTC-USDT, please use capital letters |
| limit | int | no | default: 500, maximum 1000 |

**response**

| parameter name | type | description |
| ------------- |-------|----------------------------|
| time | int64 | transaction time |
| isBuyerMaker | bool | Whether the buyer is the maker of the order (true / false) |
| price | string | transaction price |
| qty | string | transaction quantity |
| quoteQty | string | turnover |

```javascript
{
  "code":0,
  "msg":"",
  "data":[
    {
      "time": 1672025549368,
      "isBuyerMaker": true,
      "price": "16885.0",
      "qty": "3.3002",
      "quoteQty": "55723.87"
    },
    {
      "time": 1672025549368,
      "isBuyerMaker": false,
      "price": "16884.0",
      "qty": "1.9190",
      "quoteQty": "32400.40"
    }
  ]
}
```

## 5. Current Funding Rate

**HTTP Requests**

```http
    GET /openApi/swap/v2/quote/premiumIndex
```

**parameter**

| parameter name | type | required | description |
| -------|--------|-------|------|
| symbol | string | No | Trading pair, for example: BTC-USDT, please use capital letters |

**response**

| parameter name | type | description |
|-----------------|--------|----------------|
| symbol | string | trading pair, for example: BTC-USDT |
| lastFundingRate | string | Last updated funding rate |
| markPrice | string | current mark price |
| indexPrice | string | index price |
| nextFundingTime | int64 | The remaining time for the next settlement, in milliseconds |

```javascript
{
  "code":0,
  "msg":"",
  "data":[
    {
      "symbol": "BTC-USDT",
      "markPrice": "16884.5",
      "indexPrice": "16886.9",
      "lastFundingRate": "0.0001",
      "nextFundingTime": 1672041600000
    },
    {
      "symbol": "ETH-USDT",
      "markPrice": "1220.94",
      "indexPrice": "1220.68",
      "lastFundingRate": "-0.0001",
      "nextFundingTime": 1672041600000
    }
  ]
}
```

## 6. Funding Rate History 

**HTTP Requests**

  ```http
    GET /openApi/swap/v2/quote/fundingRate
  ```

**parameter**

| parameter name | type | required | description |
| -------|--------|----------|----------------------------|
| symbol | string | Yes      | trading pair, for example: BTC-USDT, please use capital letters |
| startTime | int64 | No       | Start time, unit: millisecond |
| endTime | int64 | No       | End time, unit: millisecond |
| limit | int32 | No       | default: 100 maximum: 1000 |

- If both startTime and endTime are not sent, return the latest limit data.
- If the amount of data between startTime and endTime is greater than limit, return the data in the case of startTime + limit.

**response**

| parameter name | type | description |
| ------------- |-------|------------|
| symbol | string | trading pair, for example: BTC-USDT |
| fundingRate | string | funding rate |
| fundingTime | int64 | Funding time: milliseconds |

```javascript
{
  "code":0,
  "msg":"",
  "data":[
    {
      "symbol": "BTC-USDT",
      "fundingRate": "0.0001",
      "fundingTime": 1585684800000
    },
    {
      "symbol": "BTC-USDT",
      "fundingRate": "-0.0017",
      "fundingTime": 1585713600000
    }
  ]
}
```

## 7. K-Line Data

    Get the latest Kline Data

**HTTP Requests**

  ```http
    GET /openApi/swap/v2/quote/klines
  ```

**parameter**

| parameter name | type | required | description |
| -------|--------|------|----------------------------|
| symbol | string | yes | trading pair, for example: BTC-USDT, please use capital letters |
| interval | string | yes | time interval, refer to field description |
| startTime | int64 | No | Start time, unit: millisecond |
| endTime | int64 | No | End time, unit: millisecond |
| limit | int64 | no | default: 500 maximum: 1440 |

- If startTime and endTime are not sent, the latest k-line data will be returned by default

**Remark**

| interval | field description |
|--------------|-------|
| 1m | One-minute K line |
| 3m | Three-minute K line |
| 5m | Five-minute K line |
| 15m | 15 minutes K line |
| 30m | Thirty minutes K line |
| 1h | One-hour candlestick line |
| 2h | Two-hour K-line |
| 4h | Four-hour K line |
| 6h | Six-hour K line |
| 8h | Eight-hour K-line |
| 12h | 12-hour K line |
| 1d | 1 day candlestick |
| 3d | 3-day K-line |
| 1w | Weekly K-line |
| 1M | Monthly candlestick |

**response**

| parameter name | type | description |
|-------|----|----|
| open | float64 | opening price |
| close | float64 | closing price |
| high | float64 | highest price |
| low | float64 | lowest price |
| volume | float64 | transaction volume |
| time | int64 | k-line time stamp, unit milliseconds |

```json
{
  "code": 0,
  "msg": "",
  "data": [
    {
      "open": "19396.8",
      "close": "19394.4",
      "high": "19397.5",
      "low": "19385.7",
      "volume": "110.05",
      "time": 1666583700000
    },
    {
      "open": "19394.4",
      "close": "19379.0",
      "high": "19394.4",
      "low": "19368.3",
      "volume": "167.44",
      "time": 1666584000000
    }
  ]
}

If startTime and endTime are not sent, the latest k-line data will be returned by default
{
"code": 0,
"msg": "",
"data": {
"open": "16879.5",
"close": "16877.0",
"high": "16880.0",
"low": "16876.5",
"volume": "428.44",
"time": 1672026300000
}
}
```

## 8. Get Swap Open Positions

**HTTP Requests**

```http
    GET /openApi/swap/v2/quote/openInterest
```

**parameter**

| parameter name | type | required | description |
| -------|--------|-----|------|
| symbol | string | yes | trading pair, for example: BTC-USDT, please use capital letters |

**response**

| parameter name | type | description |
|--------------|-------|------|
| openInterest | string | open interest |
| symbol | string | contract name |
| time | int64 | matching engine time |

```javascript
{
  "code": 0,
  "msg": "",
  "data": {
    "openInterest": "3289641547.10",
    "symbol": "BTC-USDT",
    "time": 1672026617364
  }
}
```

## 9. Get Ticker

**HTTP Requests**

```http
    GET /openApi/swap/v2/quote/ticker
```

**Request parameters**

| parameter name | type | required | description |
| -------|--------|----|------|
| symbol | string | No | Trading pair, for example: BTC-USDT, please use capital letters |

- If no transaction pair parameters are sent, all transaction pair information will be returned

**response**

| parameter name | type | description |
|--------------------|--------|-----------------|
| symbol | string | trading pair, for example: BTC-USDT |
| priceChange | string | 24 hour price change |
| priceChangePercent | string | price change percentage |
| lastPrice | string | latest transaction price |
| lastQty | string | latest transaction amount |
| highPrice | string | 24-hour highest price |
| lowPrice | string | 24 hours lowest price |
| volume | string | 24-hour volume |
| quoteVolume | string | 24-hour turnover, the unit is USDT |
| openPrice | string | first price within 24 hours |
| openTime | int64 | The time when the first transaction occurred within 24 hours |
| closeTime | int64 | The time when the last transaction occurred within 24 hours |

```json
{
  "code": 0,
  "msg": "",
  "data": {
    "symbol": "BTC-USDT",
    "priceChange": "52.5",
    "priceChangePercent": "0.31",
    "lastPrice": "16880.5",
    "lastQty": "2.2238",
    "highPrice": "16897.5",
    "lowPrice": "16726.0",
    "volume": "245870.1692",
    "quoteVolume": "4151395117.73",
    "openPrice": "16832.0",
    "openTime": 1672026667803,
    "closeTime": 1672026648425
  }
}

or (when not sending transaction pair information)
{
  "code": 0,
  "msg": "",
  "data": [
    {
      "symbol": "QNT-USDT",
      "priceChange": "0.40",
      "priceChangePercent": "0.38",
      "lastPrice": "106.39",
      "lastQty": "0.10",
      "highPrice": "106.70",
      "lowPrice": "104.09",
      "volume": "2350.86",
      "quoteVolume": "250131.27",
      "openPrice": "106.00",
      "openTime": 1672026684857,
      "closeTime": 1672026262497
    },
    {
      "symbol": "VET-USDT",
      "priceChange": "-0.00010",
      "priceChangePercent": "-0.62",
      "lastPrice": "0.01612",
      "lastQty": "193",
      "highPrice": "0.01627",
      "lowPrice": "0.01593",
      "volume": "21566781",
      "quoteVolume": "347658.67",
      "openPrice": "0.01622",
      "openTime": 1672026697663,
      "closeTime": 1672026488862
    }
  ]
}
```


# Account Interface

## **1. Get Perpetual Swap Account Asset Information**

```
Get asset information of user‘s Perpetual Account
```

**HTTP Requests**

```http
    GET /openApi/swap/v2/user/balance
```

**parameter**

| Parameter name | Type | Required |Description |
| ------------- |----|---|----|
| timestamp | int64 | yes | request timestamp in milliseconds |
| recvWindow | int64 | No | Request valid time window value, unit: millisecond |

**response**

| parameter name | type | description |
| ------------- |-------|----|
| code | int64 | Error code, 0 means success, not 0 means abnormal failure |
| msg | string | error message |
| asset | string | user asset |
| balance | string | asset balance |
| equity | string | net asset value |
| unrealizedProfit | string | unrealized profit and loss |
| realizedProfit | string | realized profit and loss |
| availableMargin| string | available margin |
| usedMargin | string | used margin |
| frozenMargin | string | frozen margin |

```javascript
{
  "code": 0,
  "msg": "",
  "data": {
    "balance": {
      "asset": "USDT",
      "balance": "15.6128",
      "equity": "15.6128",
      "unrealizedProfit": "0.0000",
      "realisedProfit": "0.0000",
      "availableMargin": "15.6128",
      "usedMargin": "0.0000",
      "freezedMargin": "0.0000"
    }
  }
}
```

## **2. Perpetual Swap Positions**

    Retrieve information on users' positions of Perpetual Swap.

**HTTP Requests**

```http
    GET /openApi/swap/v2/user/positions
```

**parameters**

| parameter name | type | required | description |
| ------------- |-------|------|----|
| symbol | string | No | Trading pair, for example: BTC-USDT, please use capital letters |
| timestamp | int64 | yes | request timestamp in milliseconds |
| recvWindow | int64 | No | Request valid time window value, unit: millisecond |

**response**

| parameter name | type | description |
|------------------|-------|-----------------------------|
| symbol | string | trading pair, for example: BTC-USDT |
| positionId | string | Position ID |
| positionSide | string | position direction LONG/SHORT long/short |
| isolated | bool | Whether it is isolated margin mode, true: isolated margin mode false: cross margin |
| positionAmt | string | Position Amount |
| availableAmt | string | AvailableAmt Amount |
| unrealizedProfit | string | unrealized profit and loss |
| realizedProfit | string | realized profit and loss |
| initialMargin | string | margin |
| avgPrice | string | Average opening price |
| leverage | int | leverage |

```javascript
{
   "code": 0,
   "msg": "",
   "data": [
        {
            "symbol": "BTC-USDT",
            "positionId": "12345678",
            "positionSide": "LONG",
            "isolated": true,
            "positionAmt": "123.33",
            "availableAmt": "128.99",
            "unrealizedProfit": "1.22",
            "realisedProfit": "8.1",
            "initialMargin": "123.33",
            "avgPrice": "2.2",
            "leverage": 10,
        }
    ]
}
```

## 3. Get Account Profit and Loss Fund Flow

- Query the capital flow of the perpetual contract under the current account.

**HTTP request**

```
    GET /openApi/swap/v2/user/income
```

**parameter**

| parameter name | type | required | description |
| ------------- |-------|------|----------------------------|
| symbol | string | No | Trading pair, for example: BTC-USDT, please use capital letters |
| incomeType | string | No | Income type, see remarks |
| startTime | int64 | no | start time |
| endTime | int64 | no | end time |
| limit | int64 | No | Number of result sets to return Default: 100 Maximum: 1000 |
| timestamp | int64 | yes | request timestamp in milliseconds |
| recvWindow | int64 | No | Request valid time window value, unit: millisecond |

**Remark**

| incomeType | Field description |
|-----------------|-------|
| TRANSFER | Transfer |
| REALIZED_PNL | Realized PnL |
| FUNDING_FEE | Funding Fee |
| COMMISSION | Fee |
| INSURANCE_CLEAR | Liquidation |
| TRIAL_FUND | Trial Fund |
| ADL | Automatic Deleveraging |
| SYSTEM_DEDUCTION | System deduction |

- If neither startTime nor endTime is sent, only the data of the last 7 days will be returned.
- If the incomeType is not sent, return all types of account profit and loss fund flow.
- Only keep the last 3 months data.

**response**

| parameter name | type | description |
|------------------|-------|--------------------- |
| symbol | string | trading pair, for example: BTC-USDT |
| incomeType | string | money flow type |
| income | string | The amount of capital flow, positive numbers represent inflows, negative numbers represent outflows |
| asset | string | asset content |
| info | string | Remarks, depending on the type of stream |
| time | int64 | time, unit: millisecond |
| tranId | string | transfer id |
| tradeId | string | The original transaction ID that caused the transaction |

```javascript
{
  "code": 0,
  "msg": "",
  "data": [
    {
      "symbol": "BTC-USDT",
      "incomeType": "COMMISSION",
      "income": "-0.1030",
      "asset": "USDT",
      "info": "Closing Fee",
      "time": 1676506292000,
      "tranId": "1676502895030034465_0_83302_COMMISSION",
      "tradeId": "1676502895030034465_0_83298"
    },
    {
      "symbol": "BTC-USDT",
      "incomeType": "INSURANCE_CLEAR",
      "income": "-29.2834",
      "asset": "USDT",
      "info": "Strong liquidation",
      "time": 1676506292000,
      "tranId": "1676502895030034465_0_83302_PNL",
      "tradeId": "1676502895030034465_0_83298"
    }
  ]
}
```

# Trade Interface


## 1. Trade order

- The current account places an order on the specified symbol contract. (Supports limit order, market order, market order for plan entrustment, limit order for plan entrustment, position stop profit and stop loss order, and liquidation for positions)

**HTTP Requests**

```http
    POST /openApi/swap/v2/trade/order
```

**parameter**

| parameter name | type | required | description |
|------------------|---------|------|------------------------------------------------------------------------------------|
| symbol | string | yes | trading pair, for example: BTC-USDT, please use capital letters |
| type | string | yes | order type LIMIT, MARKET, STOP_MARKET, TAKE_PROFIT_MARKET, TRIGGER_LIMIT, TRIGGER_MARKET |
| side | string | yes | buying and selling direction SELL, BUY |
| positionSide | string | No | Position direction, and only LONG or SHORT can be selected, the default is LONG |
| price | float64 | no | entrusted price |
| quantity | float64 | No | The order quantity, this parameter is not supported when using closePosition. |
| stopPrice | float64 | No | Trigger price, only required for STOP_MARKET, TAKE_PROFIT_MARKET, TRIGGER_LIMIT, TRIGGER_MARKET |
| timestamp | int64 | yes | request timestamp, unit: millisecond |
| recvWindow | int64 | No | Request valid time window value, unit: millisecond |

Depending on the order type, certain parameters are mandatory:

| Type | Mandatory Parameters |
|-----------------------------------|---------------------|
| LIMIT | quantity, price |
| MARKET | quantity |
| TRIGGER_LIMIT | quantity, stopPrice, price |
| STOP_MARKET, TAKE_PROFIT_MARKET, TRIGGER_MARKET | quantity, stopPrice |

- The triggering of the conditional order must:

  - STOP_MARKET stop loss order:
    - The accumulative quantity of the pending stop loss orders cannot be greater than the quantity of open positions
    - Buy: the mark price is higher than or equal to the trigger price stopPrice
    - Sell: the mark price is lower than or equal to the trigger price stopPrice
  - TAKE_PROFIT_MARKET take profit order:
    - The accumulative quantity of the pending take profit order cannot be greater than the position quantity
    - Buy: the mark price is lower than or equal to the trigger price stopPrice
    - Sell: the mark price is higher than or equal to the trigger price stopPrice

**response**

| parameter name | type | description |
|--------------|---------|---------------------------------------------------------------|
| symbol | string | trading pair, for example: BTC-USDT |
| side | string | buying and selling direction |
| type | string | order type |
| positionSide | string | position side |
| orderId | int64 | order number |


```javascript
{
  "code": 0,
  "msg": "",
  "data": {
    "order": {
      "symbol": "BTC-USDT",
      "orderId": 1590973236294713344,
      "side": "BUY",
      "positionSide": "LONG",
      "type": "LIMIT"
    }
  }
}
```

## 2. Bulk order

- The current account performs batch order operations on the specified symbol contract.

**HTTP Requests**

```http
    POST /openApi/swap/v2/trade/batchOrders
```

**parameter**


| parameter name | type | required | description |
|-----------------|-----------|------|-------------------------|
| batchOrders | LIST\<Order> | yes | order list, up to 5 orders are supported, the Order object reference is as follows |
| timestamp | int64 | yes | request timestamp, unit: millisecond |
| recvWindow | int64 | No | Request valid time window value, unit: millisecond |

Order object:

| parameter name | type | required | description |
|------------------|---------|------|----------------------------------------------------------------------------|
| symbol | string | yes | trading symbol, for example: BTC-USDT, please use capital letters |
| type | string | yes | order type LIMIT, MARKET, STOP_MARKET, TAKE_PROFIT_MARKET, TRIGGER_LIMIT, TRIGGER_MARKET |
| side | string | yes | trade direction, (BUY/SELL buy/sell) |
| positionSide | string | No | Position direction, and only LONG or SHORT can be selected, the default is LONG |
| price | float64 | no | entrusted price |
| quantity | float64 | No | The order quantity, this parameter is not supported when using closePosition. |
| closePosition | string | No | true, false; all positions will be closed after the trigger, only STOP_MARKET and TAKE_PROFIT_MARKET are supported; not used with quantity; it comes with a position-only effect |
| stopPrice | float64 | No | Stop profit and stop loss, plan order, trigger price, only STOP_MARKET, TAKE_PROFIT_MARKET,TRIGGER |

- Specific order conditions and rules are consistent with ordinary orders
- Batch orders are processed concurrently, and order matching order is not guaranteed


**response**

| parameter name | type | description |
|--------------|---------|---------------------------------------------------------------|
| symbol | string | trading pair, for example: BTC-USDT |
| side | string | buying and selling direction |
| type | string | order type |
| positionSide | string | position side |
| orderId | int64 | order number |

```javascript
{
  "code": 0,
  "msg": "",
  "data": {
    "orders": [
      {
        "symbol": "BTC-USDT",
        "orderId": 1590973236294713344,
        "side": "BUY",
        "positionSide": "LONG",
        "type": "LIMIT"
      }
    ]
  }
}
```

## 3. One-Click Close All Positions

- One-click liquidation of all positions under the current account. Note that one-click liquidation is triggered by a market order.

**HTTP Requests**

```http
    POST /openApi/swap/v2/trade/closeAllPositions
```

**parameter**

| parameter name | type | required | description |
| ------------- |-----|------|----------------|
| timestamp | int64 | yes | request timestamp, unit: millisecond |
| recvWindow | int64 | No | Request valid time window value, unit: millisecond |

**response**

| parameter name | type | description |
| ---- |--------------|---------------------|
| success | LIST\<int64> | Multiple order numbers generated by all one-click liquidation |
| failed | structure array | the order number of the failed position closing |

```javascript
{
  "code": 0,
  "msg": "",
  "data": {
    "success": [
      1608667648466354176
    ],
    "failed": null
  }
}
```

## 4. Cancel an Order

- Cancel an order that the current account is in the current entrusted state.

**HTTP request**

```
    DELETE /openApi/swap/v2/trade/order
```

**parameter**

| parameter name | type | required | description |
| ------------- |-------|------|---------------------------|
| orderId | int64 | yes | order number |
| symbol | string | yes | trading pair, for example: BTC-USDT, please use capital letters |
| timestamp | int64 | yes | request timestamp, unit: millisecond |
| recvWindow | int64 | No | Request valid time window value, unit: millisecond |

**response**

| parameter name | type | description |
|--------------|---------|---------------------------------------------------------------|
| time | int64 | order time, unit: millisecond |
| symbol | string | trading pair, for example: BTC-USDT |
| side | string | buying and selling direction |
| type | string | order type |
| positionSide | string | position side |
| cumQuote | string | transaction amount |
| status | string | order status |
| stopPrice | string | trigger price |
| price | string | entrusted price |
| origQty | string | original order quantity |
| avgPrice | string | average transaction price |
| executedQty | string | volume |
| orderId | int64 | order number |
| profit | string | profit and loss |
| commission | string | commission |
| updateTime | int64 | update time, unit: millisecond |

```javascript
{
  "code": 0,
  "msg": "",
  "data": {
    "order": {
      "symbol": "LINK-USDT",
      "orderId": 1597783850786750464,
      "side": "BUY",
      "positionSide": "LONG",
      "type": "TRIGGER_MARKET",
      "origQty": "5.0",
      "price": "5.0000",
      "executedQty": "0.0",
      "avgPrice": "0.0000",
      "cumQuote": "0",
      "stopPrice": "5.0000",
      "profit": "",
      "commission": "",
      "status": "CANCELLED",
      "time": 1669776330000,
      "updateTime": 1669776330000
    }
  }
}
```
## 5. Cancel a Batch of Orders

- Batch cancellation of some of the orders whose current account is in the current entrusted state.

**HTTP request**

```
    DELETE /openApi/swap/v2/trade/batchOrders
```

**parameter**

| parameter name | type | required | description |
| ------------- |-------------|------|----------------------------------|
| symbol | string | yes | trading pair, for example: BTC-USDT, please use capital letters |
| orderIdList | LIST\<int64> | yes | system order number, up to 10 orders [1234567,2345678] |
| timestamp | int64 | yes | request timestamp, unit: millisecond |
| recvWindow | int64 | No | Request valid time window value, unit: millisecond |

**response**

| parameter name | type | description |
|---- |--------------|-----------------|
| code | int64 | Error code, 0 means success, not 0 means abnormal failure |
| msg | string | error message |
| success | LIST\<order> | list of successfully canceled orders |
| failed | structure array | list of failed orders |
| orderId | int64 | order number |
| errorCode | int64 | Error code, 0 means success, not 0 means abnormal failure |
| errorMessage | string | error message |
- The order object is as follows

| parameter name | type | description |
|--------------|---------|---------------------------------------------------------------|
| time | int64 | order time, unit: millisecond |
| symbol | string | trading pair, for example: BTC-USDT |
| side | string | buying and selling direction |
| type | string | order type |
| positionSide | string | position side |
| cumQuote | string | transaction amount |
| status | string | order status |
| stopPrice | string | trigger price |
| price | string | entrusted price |
| origQty | string | original order quantity |
| avgPrice | string | average transaction price |
| executedQty | string | volume |
| orderId | int64 | order number |
| profit | string | profit and loss |
| commission | string | commission |
| updateTime | int64 | update time, unit: millisecond |
```javascript
{
  "code": 0,
  "msg": "",
  "data": {
    "success": [
      {
        "symbol": "LINK-USDT",
        "orderId": 1597783850786750464,
        "side": "BUY",
        "positionSide": "LONG",
        "type": "TRIGGER_MARKET",
        "origQty": "5.0",
        "price": "5.5710",
        "executedQty": "0.0",
        "avgPrice": "0.0000",
        "cumQuote": "0",
        "stopPrice": "5.0000",
        "profit": "0.0000",
        "commission": "0.000000",
        "status": "CANCELLED",
        "time": 1669776330000,
        "updateTime": 1672370837000
      }
    ],
    "failed": null
  }
}
```

## 6. Cancel All Orders

- Cancel all orders in the current entrusted state of the current account.

**HTTP request**

```
    DELETE /openApi/swap/v2/trade/allOpenOrders
```

**parameter**

| parameter name | type | required | description |
|------------|--------|-----|---------------------------|
| symbol | string | yes | trading pair, for example: BTC-USDT, please use capital letters |
| timestamp | int64 | yes | request timestamp, unit: millisecond |
| recvWindow | int64 | No | Request valid time window value, unit: millisecond |

**response**

| parameter name | type | description |
|--------------|--------------|-----------------|
| success | LIST\<order> | list of successfully canceled orders |
| failed | structure array | list of failed orders |
| orderId | int64 | order number |
| errorCode | int64 | Error code, 0 means success, not 0 means abnormal failure |
| errorMessage | string | error message |
- The order object is as follows

| parameter name | type | description |
|--------------|---------|---------------------------------------------------------------|
| time | int64 | order time, unit: millisecond |
| symbol | string | trading pair, for example: BTC-USDT |
| side | string | buying and selling direction |
| type | string | order type |
| positionSide | string | position side |
| cumQuote | string | transaction amount |
| status | string | order status |
| stopPrice | string | trigger price |
| price | string | entrusted price |
| origQty | string | original order quantity |
| avgPrice | string | average transaction price |
| executedQty | string | volume |
| orderId | int64 | order number |
| profit | string | profit and loss |
| commission | string | commission |
| updateTime | int64 | update time, unit: millisecond |
```javascript
{
  "code": 0,
  "msg": "",
  "data": {
    "success": [
      {
        "symbol": "LINK-USDT",
        "orderId": 1597783835095859200,
        "side": "BUY",
        "positionSide": "LONG",
        "type": "TRIGGER_LIMIT",
        "origQty": "5.0",
        "price": "9.0000",
        "executedQty": "0.0",
        "avgPrice": "0.0000",
        "cumQuote": "0",
        "stopPrice": "9.5000",
        "profit": "",
        "commission": "",
        "status": "NEW",
        "time": 1669776326000,
        "updateTime": 1669776326000
      }
    ],
    "failed": null
  }
}
```

## 7. Query pending orders

- Query all orders that the user is currently entrusted with.

**HTTP request**

```
    GET /openApi/swap/v2/trade/openOrders
```

**parameters**

| parameter name | type | required | field description
|---------|---------|------|---------------------------|
| symbol | string | No | Trading pair, for example: BTC-USDT, please use capital letters |
| timestamp | int64 | yes | request timestamp, unit: millisecond |
| recvWindow | int64 | No | Request valid time window value, unit: millisecond |

- Without the symbol parameter, it will return the pending orders of all trading pairs

**response**

| parameter name | type | description |
|--------------|---------|---------------------------------------------------------------|
| time | int64 | order time, unit: millisecond |
| symbol | string | trading pair, for example: BTC-USDT |
| side | string | buying and selling direction |
| type | string | order type |
| positionSide | string | position side |
| cumQuote | string | transaction amount |
| status | string | order status |
| stopPrice | string | trigger price |
| price | string | entrusted price |
| origQty | string | original order quantity |
| avgPrice | string | average transaction price |
| executedQty | string | volume |
| orderId | int64 | order number |
| profit | string | profit and loss |
| commission | string | commission |
| updateTime | int64 | update time, unit: millisecond |

```javascript
{
  "code": 0,
  "msg": "",
  "data": {
    "orders": [
      {
        "symbol": "LINK-USDT",
        "orderId": 1597783850786750464,
        "side": "BUY",
        "positionSide": "LONG",
        "type": "TRIGGER_MARKET",
        "origQty": "5.0",
        "price": "5.0000",
        "executedQty": "0.0",
        "avgPrice": "0.0000",
        "cumQuote": "0",
        "stopPrice": "5.0000",
        "profit": "0.0",
        "commission": "0.0",
        "status": "NEW",
        "time": 1669776330000,
        "updateTime": 1669776330000
      },
      {
        "symbol": "LINK-USDT",
        "orderId": 1597783835095859200,
        "side": "BUY",
        "positionSide": "LONG",
        "type": "TRIGGER_LIMIT",
        "origQty": "5.0",
        "price": "9.0000",
        "executedQty": "0.0",
        "avgPrice": "0.0000",
        "cumQuote": "0",
        "stopPrice": "9.5000",
        "profit": "0.0",
        "commission": "0.0",
        "status": "NEW",
        "time": 1669776326000,
        "updateTime": 1669776326000
      }
    ]
  }
}
 ```

## 8. Query Order

- Query order details

**HTTP request**

```
    GET /openApi/swap/v2/trade/order
```

**parameter**

| parameter name | type | required | description |
| ------------- |-------|------|--------------------------|
| symbol | string | yes | trading pair, for example: BTC-USDT, please use capital letters |
| orderId | int64 | yes | order number |
| timestamp | int64 | yes | request timestamp, unit: millisecond |
| recvWindow | int64 | No | Request valid time window value, unit: millisecond |

**response**

| parameter name | type | description |
|--------------|---------|---------------------------------------------------------------|
| time | int64 | order time, unit: millisecond |
| symbol | string | trading pair, for example: BTC-USDT |
| side | string | buying and selling direction |
| type | string | order type |
| positionSide | string | position side |
| cumQuote | string | transaction amount |
| status | string | order status |
| stopPrice | string | trigger price |
| price | string | entrusted price |
| origQty | string | original order quantity |
| avgPrice | string | average transaction price |
| executedQty | string | volume |
| orderId | int64 | order number |
| profit | string | profit and loss |
| commission | string | commission |
| updateTime | int64 | update time, unit: millisecond |

```javascript
{
  "code": 0,
  "msg": "",
  "data": {
    "order": {
      "symbol": "BTC-USDT",
      "orderId": 1597597642269917184,
      "side": "SELL",
      "positionSide": "LONG",
      "type": "TAKE_PROFIT_MARKET",
      "origQty": "1.0000",
      "price": "0.0",
      "executedQty": "0.0000",
      "avgPrice": "0.0",
      "cumQuote": "",
      "stopPrice": "16494.0",
      "profit": "",
      "commission": "",
      "status": "FILLED",
      "time": 1669731935000,
      "updateTime": 1669752524000
    }
  }
}
```

## 9. Query Margin Mode

- Query the user's margin mode on the specified symbol contract: isolated or cross.

**HTTP request**

```
    GET /openApi/swap/v2/trade/marginType
```

**parameter**

| parameter name | type | required | description |
| ------------- |-------|------|-----------------------------------|
| symbol | string | yes | trading pair, for example: BTC-USDT, please use capital letters |
| timestamp | int64 | yes | request timestamp, unit: millisecond |
| recvWindow | int64 | No | Request valid time window value, unit: millisecond |

**response**

| parameter name | type | description |
| ----------- |--------| -------- |
| marginType | string | margin mode |

**Remark**

| marginType | Field description |
| ----------|----|
| ISOLATED | Isolated Margin |
| CROSSED | Full position |

```javascript
{
  "code": 0,
  "msg": "",
  "data": {
    "marginType": "CROSSED"
  }
}
```

## 10. Switch Margin Mode

- Change the user's margin mode on the specified symbol contract: isolated margin or cross margin.

**HTTP request**

```
    POST /openApi/swap/v2/trade/marginType
```

**parameter**

| parameter name | type | required | description |
| ------------- |-------|------|-----------------------------------|
| symbol | string | yes | trading pair, for example: BTC-USDT, please use capital letters |
| marginType | string | Yes | Margin mode ISOLATED (isolated margin), CROSSED (cross margin) |
| timestamp | int64 | yes | request timestamp, unit: millisecond |
| recvWindow | int64 | No | Request valid time window value, unit: millisecond |

**response**

| parameter name | type | description |
| ---- |--------| ---- |
| code | int64 | Error code, 0 means success, not 0 means abnormal failure |
| msg | string | error message |

```javascript
{
  "code": 0,
  "msg": ""
}
```

## 11. Query Leverage

- Query the opening leverage of the user in the specified symbol contract.

**HTTP request**

```
    GET /openApi/swap/v2/trade/leverage
```

**parameter**

| parameter name | type | required | description |
| --------- |--------|------|---------------------------|
| symbol | string | yes | trading pair, for example: BTC-USDT, please use capital letters |
| timestamp | int64 | yes | request timestamp, unit: millisecond |
| recvWindow | int64 | No | Request valid time window value, unit: millisecond |

**response**

| parameter name | type | description |
| ------------- |-------| ---------- |
| longLeverage | int64 | Long position leverage |
| shortLeverage | int64 | Short Leverage |

```javascript
{
  "code": 0,
  "msg": "",
  "data": {
    "longLeverage": 6,
    "shortLeverage": 6
  }
}
```

## 12. Switch Leverage 

- Adjust the user's opening leverage in the specified symbol contract.

**HTTP request**

```
    POST /openApi/swap/v2/trade/leverage
```

**parameter**

| parameter name | type | required | description |
| ------------- |-------|------|----------------------------|
| symbol | string | yes | trading pair, for example: BTC-USDT, please use capital letters |
| side | string | Yes | Leverage for long or short positions, LONG for long positions, SHORT for short positions |
| leverage | int64 | yes | leverage |
| timestamp | int64 | yes | request timestamp, unit: millisecond |
| recvWindow | int64 | No | Request valid time window value, unit: millisecond |

**response**

| parameter name | type | description |
|--------|--------|-----|
| leverage | int64 | leverage |
| symbol | string | trading pair |
```javascript
{
  "code": 0,
  "msg": "",
  "data": {
    "leverage": 6,
    "symbol": "BTC-USDT"
  }
}
```

## 13. User's Force Orders

- Query the user's forced liquidation order.

**HTTP request**

```
    GET /openApi/swap/v2/trade/forceOrders
```

**parameter**

| parameter name | type | required | description |
|---------------|------|------|------------------------------|
| symbol | string | No | Trading pair, for example: BTC-USDT, please use capital letters |
| autoCloseType | string | No | "LIQUIDATION": liquidation order, "ADL": ADL liquidation order |
| startTime | int64 | No | Start time, unit: millisecond |
| endTime | int64 | No | End time, unit: millisecond |
| limit | int | No | The number of returned result sets The default value is 50, the maximum value is 100 |
| timestamp | int64 | Yes | The timestamp of the request, unit: millisecond |
| recvWindow | int64 | No | Request valid time window value, unit: millisecond |

- If "autoCloseType" is not passed, both forced liquidation orders and ADL liquidation orders will be returned
- If "startTime" is not passed, only the data within 7 days before "endTime" will be returned

**response**

| parameter name | type | description |
|--------------|---------|---------------------------------------------------------------|
| time | int64 | order time, unit: millisecond |
| symbol | string | trading pair, for example: BTC-USDT |
| side | string | buying and selling direction |
| type | string | order type |
| positionSide | string | position side |
| cumQuote | string | transaction amount |
| status | string | order status |
| stopPrice | string | trigger price |
| price | string | entrusted price |
| origQty | string | original order quantity |
| avgPrice | string | average transaction price |
| executedQty | string | volume |
| orderId | int64 | order number |
| profit | string | profit and loss |
| commission | string | commission |
| updateTime | int64 | update time, unit: millisecond |

```javascript
{
  "code": 0,
  "msg": "",
  "data": {
    "orders": [
      {
        "symbol": "BTC-USDT",
        "orderId": 1580653094914490368,
        "side": "SELL",
        "positionSide": "LONG",
        "type": "MARKET",
        "origQty": "0.0600",
        "price": "20798.4",
        "executedQty": "0.0600",
        "avgPrice": "20798.3",
        "cumQuote": "1248",
        "stopPrice": "",
        "profit": "-9.5605",
        "commission": "-0.499161",
        "status": "FILLED",
        "time": 1665653919000,
        "updateTime": 1665740319000
      }
    ]
  }
}
```

## 14. User's History Orders

- Query the user's historical orders (order status is completed or canceled).

**HTTP request**

```
    GET /openApi/swap/v2/trade/allOrders
```

**parameter**

| parameter name | type | required | description |
|------------|--------|------|-----------------------|
| symbol | string | yes | trading pair, for example: BTC-USDT, please use capital letters |
| orderId | int64 | No | Only return this orderID and subsequent orders, and return the latest order by default |
| startTime | int64 | No | Start time, unit: millisecond |
| endTime | int64 | No | End time, unit: millisecond |
| limit | int64 | yes | number of result sets to return Default: 500 Maximum: 1000 |
| timestamp | int64 | yes | request timestamp, unit: millisecond |
| recvWindow | int64 | No | Timestamp of the request, unit: millisecond |

- The maximum query time range shall not exceed 7 days
- Query data within the last 7 days by default
  
**response**

| parameter name | type | description |
|--------------|---------|---------------------------------------------------------------|
| time | int64 | order time, unit: millisecond |
| symbol | string | trading pair, for example: BTC-USDT |
| side | string | buying and selling direction |
| type | string | order type |
| positionSide | string | position side |
| cumQuote | string | transaction amount |
| status | string | order status |
| stopPrice | string | trigger price |
| price | string | entrusted price |
| origQty | string | original order quantity |
| avgPrice | string | average transaction price |
| executedQty | string | volume |
| orderId | int64 | order number |
| profit | string | profit and loss |
| commission | string | commission |
| updateTime | int64 | update time, unit: millisecond |
```javascript
{
  "code": 0,
  "msg": "",
  "data": {
    "orders": [
      {
        "symbol": "LINK-USDT",
        "orderId": 1585839271162413056,
        "side": "BUY",
        "positionSide": "LONG",
        "type": "TRIGGER_MARKET",
        "origQty": "5.0",
        "price": "9",
        "executedQty": "0.0",
        "avgPrice": "0",
        "cumQuote": "0",
        "stopPrice": "5",
        "profit": "0.0000",
        "commission": "0.000000",
        "status": "CANCELLED",
        "time": 1667631605000,
        "updateTime": 1667631605000
      },
      {
        "symbol": "BTC-USDT",
        "orderId": 1588430651630026752,
        "side": "SELL",
        "positionSide": "SHORT",
        "type": "LIMIT",
        "origQty": "0.0100",
        "price": "20668.0",
        "executedQty": "0.0100",
        "avgPrice": "20668.0",
        "cumQuote": "207",
        "stopPrice": "",
        "profit": "0.0000",
        "commission": "-0.041336",
        "status": "FILLED",
        "time": 1667546354000,
        "updateTime": 1667565512000
      }
    ]
  }
}
```

## 15. Adjust isolated margin

- Adjust the isolated margin funds for the positions in the isolated position mode.

**HTTP request**

```
    POST /openApi/swap/v2/trade/positionMargin
```

**parameter**

| parameter name | type | required | description |
|------------|---------|------|------------------------------|
| symbol | string | yes | trading pair, for example: BTC-USDT, please use capital letters |
| amount | float64 | yes | margin funds |
| type | int | yes | adjustment direction 1: increase isolated margin, 2: decrease isolated margin |
| positionSide | string | No | Position direction, and only LONG or SHORT can be selected, the default is LONG |
| timestamp | int64 | yes | request timestamp, unit: millisecond |
| recvWindow | int64 | No | Request valid time window, unit: millisecond |

**response**

| parameter name | type | description |
|-----------------|---------|--------------------- |
| code | int64 | Error code, 0 means success, not 0 means abnormal failure |
| msg | string | error message |
| amount | float64 | margin funds |
| type | int | Adjustment direction 1: increase isolated margin, 2: decrease isolated margin |
```javascript
{
  "code": 0,
  "msg": "",
  "amount": 1,
  "type": 1
}
```

# Other Interface

The base URL of Websocket Market Data is: `wss://open-api-swap.bingx.com/swap-market`

User Data Streams are accessed at `/swap-market?listenKey=`

```
wss://open-api-swap.bingx.com/swap-market?listenKey=a8ea75681542e66f1a50a1616dd06ed77dab61baa0c296bca03a9b13ee5f2dd7
```

Use following API to fetch and update listenKey:

## generate Listen Key

listen key Valid for 1 hour

**interface**
```
    POST /openApi/user/auth/userDataStream
```

CURL

```
curl -X POST 'https://open-api.bingx.com/openApi/user/auth/userDataStream' --header "X-BX-APIKEY:g6ikQYpMiWLecMQ39DUivd4ENem9ygzAim63xUPFhRtCFBUDNLajRoZNiubPemKT"

```

**request header parameters**

| parameter name          | type   | Is it required | Remark         |
| ------         |--------|----------------|------------|    
| X-BX-APIKEY    | string | yes            | API KEY |


**response**

| parameter name                | type   | Remark     |
| ------               |--------|------------|    
| listenKey               | string | listen Key |


```
{"listenKey":"a8ea75681542e66f1a50a1616dd06ed77dab61baa0c296bca03a9b13ee5f2dd7"}
```


## extend Listen Key Validity period

The validity period is extended to 60 minutes after this call, and it is recommended to send a ping every 30 minutes.

**interface**
```
    PUT /openApi/user/auth/userDataStream
```

```
curl -i -X PUT 'https://open-api.bingx.com/openApi/user/auth/userDataStream?listenKey=d84d39fe78762b39e202ba204bf3f7ebed43bbe7a481299779cb53479ea9677d'
```

**request parameters**

| parameter name          | type   | Is it required | Remark         |
| ------         | ------  |----------------|------------|    
| listenKey   | string  | yes            | listenKey |


**response**

```
http status 200 success
http status 204 not content
http status 404 not find key
```

## delete Listen Key

delete User data flow.

**interface**
```
    DELETE /openApi/user/auth/userDataStream
```

```
curl -i -X DELETE 'https://open-api.bingx.com/openApi/user/auth/userDataStream?listenKey=d84d39fe78762b39e202ba204bf3f7ebed43bbe7a481299779cb53479ea9677d'
```

**request parameters**

| parameter name          | type   | Is it required | Remark        |
| ------         | ------  |----------------|-----------|    
| listenKey   | string  | yes            | listenKey |


**response**

```
http status 200 success
http status 204 not content
http status 404 not find key
```