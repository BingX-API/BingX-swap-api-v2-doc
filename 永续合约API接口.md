Bingx官方API文档
==================================================
Bingx开发者文档([English Docs](./Perpetual_Swap_API_Documentation.md))。

<!-- TOC -->
- [介绍](#介绍)
- [接口说明](#接口说明)
- [签名认证](#签名认证)
  - [创建API](#创建api)
  - [权限设置](#权限设置)
  - [请求内容](#请求内容)
  - [签名说明](#签名说明)
  - [请求交互](#请求交互)
- [基础信息](#基础信息)
  - [常见错误码](#常见错误码)
  - [时间戳规范](#时间戳规范)
    - [例子](#例子)
  - [数字规范](#数字规范)
  - [频率限制](#频率限制)
    - [REST API](#rest-api)
  - [查询系统时间](#查询系统时间)
- [行情接口](#行情接口)
  - [1. 查询合约基础信息](#1-查询合约基础信息)
  - [2. 最新价格](#2-最新价格)
  - [3. 深度信息](#3-深度信息)
  - [4. 近期成交](#4-近期成交)
  - [5. 最新标记价格和资金费率](#5-最新标记价格和资金费率)
  - [6. 查询资金费率历史](#6-查询资金费率历史)
  - [7. K线数据](#7-K线数据)
  - [8. 获取合约未平仓数](#8-获取合约未平仓数)
  - [9. 24小时价格变动情况](#9-24小时价格变动情况)
- [账户接口](#账户接口)
  - [1. 查询账户信息](#1-查询账户信息)
  - [2. 查询持仓信息](#2-查询持仓信息)
- [交易接口](#交易接口)
  - [1. 交易下单](#1-交易下单)
  - [2. 批量下单](#2-批量下单)
  - [3. 全部一键平仓下单](#3-全部一键平仓下单)
  - [4. 撤销订单](#4-撤销订单)
  - [5. 批量撤销订单](#5-批量撤销订单)
  - [6. 撤销全部订单](#6-撤销全部订单)
  - [7. 查询当前全部挂单](#7-查询当前全部挂单)
  - [8. 查询订单](#8-查询订单)
  - [9. 查询逐全仓模式](#9-查询逐全仓模式)
  - [10. 变换逐全仓模式](#10-变换逐全仓模式)
  - [11. 查询开仓杠杆](#11-查询开仓杠杆)
  - [12. 调整开仓杠杆](#12-调整开仓杠杆)
  - [13. 用户强平单历史](#13-用户强平单历史)
  - [14. 查询历史订单](#14-查询历史订单)
  - [15. 调整逐仓保证金](#15-调整逐仓保证金)


<!-- /TOC -->

# 介绍

欢迎使用[Bingx](https://bingx.com)开发者文档。

本文档提供了永续合约交易业务的账户管理、行情查询、交易功能等相关API的使用方法介绍。
行情API提供市场的公开的行情数据接口，账户和交易API需要身份验证，提供下单、撤单，查询订单和帐户信息等功能。


# 接口说明
- GET方法的接口, 参数必须在query string中发送.
- POST, PUT, 和 DELETE 方法的接口, 参数可以在 query string 中发送，也可以在 request body 中发送(content type application/x-www-form-urlencoded)。允许混合这两种方式发送参数。但如果同一个参数名在 query string 和 request body 中都有，query string 中的会被优先采用。
- 对参数的顺序不做要求。

# 签名认证
## 创建API

- 很多接口需要API Key才可以访问,在对请求进行签名之前，您必须通过 [Bingx](https://bingx.com/zh-hk/account/api/) 网站【用户中心】-【API管理】创建一个API key。 创建key后，您将获得2个必须记住的信息：
API key和Secret key
* 设置API key的同时，为了安全，建议设置IP访问白名单.
* 永远不要把你的API key/Secret key告诉给任何人

```
 如果不小心泄露了API key，请立刻删除此API key, 并可以另外生产新的API key.
```
## 权限设置
* 新创建的API的默认权限是 只读。
* 如果需要通过API进行下单交易等写操作，需要在UI修改为对应权限。

## 请求内容

请求需要鉴权的接口必须包含以下信息：

* 请求头带上 X-BX-APIKEY 传递 API Key。
* 请求参数带上 signature 使用签名算法得出的签名。
* timestamp 作为您的请求的时间戳,单位是毫秒。服务器收到请求时会判断请求中的时间戳，如果是5000毫秒之前发出的，则请求会被认为无效。这个时间空窗值可以通过发送可选参数recvWindow来定义。

## 签名说明
signature 请求参数使用**HMAC SHA256**方法加密而得到的。

**例如：对于调整币种杠杆请求参数进行签名**
- 接口参数:
```
symbol=BTC-USDT
timestamp=1667872120843
side=LONG
leverage=6
```
- api信息:
```
apiKey = hO6oQotzTE0S5FRYze2Jx2wGx7eVnJGMolpA1nZyehsoMgCcgKNWQHd4QgTFZuwl4Zt4xMe2PqGBegWXO4A
secretKey = mheO6dR8ovSsxZQCOYEFCtelpuxcWGTfHw7te326y6jOwq5WpvFQ9JNljoTwBXZGv5It07m9RXSPpDQEK2w
```
- 参数通过`query string`发送示例
```
1. 对接口参数进行拼接: symbol=BTC-USDT&timestamp=1667872120843&side=LONG&leverage=6
2. 对拼接好的参数字符串使用secretKey生成签名: 4f581ecdb1fa09b9d6e57886b6f70cffed17f82b93399722939e49a38edec2bf
   echo -n "symbol=BTC-USDT&timestamp=1667872120843&side=LONG&leverage=6" | openssl dgst -sha256 -hmac "mheO6dR8ovSsxZQCOYEFCtelpuxcWGTfHw7te326y6jOwq5WpvFQ9JNljoTwBXZGv5It07m9RXSPpDQEK2w" -hex
3. 发送请求: curl -H 'X-BX-APIKEY: hO6oQotzTE0S5FRYze2Jx2wGx7eVnJGMolpA1nZyehsoMgCcgKNWQHd4QgTFZuwl4Zt4xMe2PqGBegWXO4A' 'https://open-api.bingx.com/openApi/swap/v2/trade/leverage?symbol=BTC-USDT&timestamp=1667872120843&side=LONG&leverage=6&signature=4f581ecdb1fa09b9d6e57886b6f70cffed17f82b93399722939e49a38edec2bf'
```
- 参数通过`request body`发送示例
```
1. 对接口参数进行拼接: symbol=BTC-USDT&timestamp=1667872120843&side=LONG&leverage=6
2. 对拼接好的参数字符串使用secretKey生成签名: 4f581ecdb1fa09b9d6e57886b6f70cffed17f82b93399722939e49a38edec2bf
   echo -n "symbol=BTC-USDT&timestamp=1667872120843&side=LONG&leverage=6" | openssl dgst -sha256 -hmac "mheO6dR8ovSsxZQCOYEFCtelpuxcWGTfHw7te326y6jOwq5WpvFQ9JNljoTwBXZGv5It07m9RXSPpDQEK2w" -hex
3. 发送请求: curl -H 'X-BX-APIKEY: hO6oQotzTE0S5FRYze2Jx2wGx7eVnJGMolpA1nZyehsoMgCcgKNWQHd4QgTFZuwl4Zt4xMe2PqGBegWXO4A' -X POST 'https://open-api.bingx.com/openApi/swap/v2/trade/leverage' -d 'symbol=BTC-USDT&timestamp=1667872120843&side=LONG&leverage=6&signature=4f581ecdb1fa09b9d6e57886b6f70cffed17f82b93399722939e49a38edec2bf'
```
- 参数通过`query string`和`request body`发送示例
```
queryString: symbol=BTC-USDT&timestamp=1668159715051
requestBody: side=LONG&leverage=6

1. 对接口参数进行拼接: symbol=BTC-USDT&timestamp=1668159715051side=LONG&leverage=6
2. 对拼接好的参数字符串使用secretKey生成签名: 8b756b01e7a30f02e19c58a91ab01b29528694316b08a51ecb8dd072942bd47d
   echo -n "symbol=BTC-USDT&timestamp=1668159715051side=LONG&leverage=6" | openssl dgst -sha256 -hmac "mheO6dR8ovSsxZQCOYEFCtelpuxcWGTfHw7te326y6jOwq5WpvFQ9JNljoTwBXZGv5It07m9RXSPpDQEK2w" -hex
3. 发送请求: curl -H 'X-BX-APIKEY: hO6oQotzTE0S5FRYze2Jx2wGx7eVnJGMolpA1nZyehsoMgCcgKNWQHd4QgTFZuwl4Zt4xMe2PqGBegWXO4A' -X POST 'https://open-api.bingx.com/openApi/swap/v2/trade/leverage?symbol=BTC-USDT&timestamp=1668159715051&signature=8b756b01e7a30f02e19c58a91ab01b29528694316b08a51ecb8dd072942bd47d' -d 'side=LONG&leverage=6'
```

## 请求交互  

REST访问的根URL：`https://open-api.bingx.com`

**请求交互说明**

1、请求参数：根据接口请求参数规定进行参数封装。

2、提交请求参数：将封装好的请求参数通过POST/GET/DELETE等方式提交至服务器。

3、服务器响应：服务器首先对用户请求数据进行参数安全校验，通过校验后根据业务逻辑将响应数据以JSON格式返回给用户。

4、数据处理：对服务器响应数据进行处理。

**成功**

HTTP状态码200表示成功响应，并可能包含内容。如果响应含有内容，则将显示在相应的返回内容里面。

# 基础信息
## 常见错误码

### 常见HTTP类型:
* 4XX 错误码用于指示错误的请求内容、行为、格式

* 5XX 错误码用于指示Bingx服务侧的问题

### 常见HTTP错误码:
* 400 Bad Request – Invalid request format 请求格式无效

* 401 Unauthorized – Invalid API Key 无效的API Key

* 403 Forbidden – You do not have access to the requested resource 请求无权限

* 404 - Not Found 没有找到请求

* 429 - Too Many Requests 请求太频繁被系统限流

* 418 - 表示收到429后继续访问，于是被封了

* 500 - Internal Server Error – We had a problem with our server 服务器内部错误

* 504 - 表示API服务端已经向业务核心提交了请求但未能获取响应(特别需要注意的是504代码不代表请求失败，而是未知。很可能已经得到了执行，也有可能执行失败，需要做进一步确认)

### 常见业务错误码:
* 100001 - 签名验证失败
* 100500 - 内部系统错误
* 80001 - 请求失败
* 80012 - 服务不可用
* 80014 - 参数无效
* 80016 - 订单不存在
* 80017 - 仓位不存在


### 注意:
* 如果失败，response body 带有错误描述信息

* 每个接口都有可能抛出异常


## 时间戳规范

除非另外指定，API中的所有时间戳均以毫秒为单位返回。

请求的时间戳必须在API服务时间的5秒内，否则请求将被视为过期并被拒绝。如果本地服务器时间和API服务器时间之间存在较大的偏差，那么我们建议您使用通过查询API服务器时间来更新http header。

### 例子

1587091154123

## 数字规范

为了保持跨平台时精度的完整性，十进制数字作为字符串返回。建议您在发起请求时也将数字转换为字符串以避免截断和精度错误。 

整数（如交易编号和顺序）不加引号。

## 频率限制

如果请求过于频繁系统将自动限制请求。

### REST API

* 行情接口：通过IP限制公共接口的调用，每1秒最多60个请求。

* 账户和交易接口：通过用户ID限制私人接口的调用，每1秒最多20个请求。

* 某些接口的特殊限制在具体的接口上注明

## 查询系统时间

**HTTP请求**

```
    GET /openApi/v2/common/server/time
```

**参数**

    无

**返回值说明**

| 参数名 | 参数类型   | 描述 |
| ------------- |--------|----|
| code        | int64  | 错误码，0表示成功，不为0表示异常失败 |
| msg         | string | 错误信息提示 |
| serverTime | int64  | 系统当前时间，单位毫秒 |

```javascript
{
  "code": 0,
  "msg": "",
  "data": {
  "serverTime": 1672025091160
  }
}
```

# 行情接口

## 1. 查询合约基础信息     

**HTTP请求**

```
    GET /openApi/swap/v2/quote/contracts
```


**参数**

    无

**响应**

| 参数名     | 类型      |       字段说明        |
|-------------------|---------|:-----------------:|
| contractId   | string  |       合约ID        |
| symbol           | string  | 交易对, 例如: BTC-USDT  |
| size              | string  | 合约大小，例如0.0001 BTC |
| quantityPrecision | int     |      交易数量精度       |
| pricePrecision    | int     |       价格精度        |
| feeRate           | float64 |       交易手续费       |
| tradeMinLimit     | int     |    交易最小单位，单位为张    |
| currency          | string  |    结算和保证金货币资产     |
| asset             | string  |      合约交易资产       |
| status            | int     |     0下线, 1上线      |
| maxLongLeverage   | int     |    多头交易的最大杠杆倍数    |
| maxShortLeverage  | int     |    空头交易的最大杠杆倍数    |

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

## 2. 最新价格

**HTTP请求**

```
    GET /openApi/swap/v2/quote/price
```

**参数**

| 参数名  | 类型     | 是否必填 |  描述 |
| -------|--------|----------|------|
| symbol | string | 否    |交易对, 例如: BTC-USDT, 请使用大写字母 |

- 不发送交易对参数，则会返回所有交易对信息

**响应**

| 参数名    | 类型     | 描述     |
|--------|--------|--------|
| symbol | string | 交易对, 例如: BTC-USDT |
| price  | string | 价格     |
| time   | int64  | 撮合引擎时间 |

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
## 3. 深度信息

**HTTP请求**

```
    GET /openApi/swap/v2/quote/depth
```

**参数**

| 参数名 | 类型     | 是否必填 | 描述                   |
| ------------- |--------|----|----------------------|
| symbol | string | 是    | 交易对, 例如: BTC-USDT, 请使用大写字母 |
| limit | int    | 否    | 默认20，可选值:[5, 10, 20, 50, 100, 500, 1000]           |

**响应**

| 参数名  | 类型     | 描述                   |
|------|--------|----------------------|
| T    | int64  | 系统时间,单位：毫秒           |
| asks | 数组     | 卖方深度。第一个元素价格，第二个元素数量 |
| bids | 数组     | 买方深度。第一个元素价格，第二个元素数量 |


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

## 4. 近期成交

**HTTP请求**

```
    GET /openApi/swap/v2/quote/trades
```

**参数**

| 参数名  | 类型     | 是否必填 | 描述 |
| -------|--------|-------|------|
| symbol | string | 是    | 交易对, 例如: BTC-USDT, 请使用大写字母 |
| limit | int    | 否    | 默认:500，最大1000 |

**响应**

| 参数名 | 类型     | 描述                         |
| ------------- |--------|----------------------------|
| time      | int64  | 成交时间                       |
| isBuyerMaker | bool   | 买方是否为挂单方(true / false) |
| price     | string | 成交价格                       |
| qty    | string | 成交数量                       |
| quoteQty    | string | 成交额                        |

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

## 5. 最新标记价格和资金费率

**HTTP请求**

```
    GET /openApi/swap/v2/quote/premiumIndex
```

**参数**

| 参数名  | 类型     | 是否必填 | 描述 |
| -------|--------|-------|------|
| symbol | string | 否    | 交易对, 例如: BTC-USDT, 请使用大写字母 |

**响应**

| 参数名             | 类型     | 描述             |
|-----------------|--------|----------------|
| symbol          | string | 交易对, 例如: BTC-USDT  |
| lastFundingRate | string | 最近更新的资金费率      |
| markPrice       | string | 当前的标记价格        |
| indexPrice       | string | 指数价格           |
| nextFundingTime | int64  | 下次结算剩余时间，单位为毫秒 |

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

## 6. 查询资金费率历史

**HTTP请求**

```
    GET /openApi/swap/v2/quote/fundingRate
```

**参数**

| 参数名  | 类型     | 是否必填 | 描述                         |
| -------|--------|-------|----------------------------|
| symbol | string | 是    | 交易对, 例如: BTC-USDT, 请使用大写字母 |
| startTime | int64  | 否    | 起始时间，单位：毫秒                 |
| endTime | int64  | 否    | 结束时间，单位：毫秒                 |
| limit | int32  | 否    | 默认值:100 最大值:1000     |

 - 如果 startTime 和 endTime 都未发送, 返回最近 limit 条数据.
 - 如果 startTime 和 endTime 之间的数据量大于 limit, 返回 startTime + limit情况下的数据。

**响应**

| 参数名 | 类型     | 描述         |
| ------------- |--------|------------|
| symbol   | string | 交易对, 例如: BTC-USDT      |
| fundingRate     | string | 资金费率       |
| fundingTime          | int64  | 资金费时间：单位毫秒 |

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

## 7. K线数据

- 查询成交价格的K线数据。

**HTTP请求**

```
    GET /openApi/swap/v2/quote/klines
```

**参数**

| 参数名  | 类型     | 是否必填 | 描述                         |
| -------|--------|------|----------------------------|
| symbol | string | 是    | 交易对, 例如: BTC-USDT, 请使用大写字母 |
| interval | string | 是    | 时间间隔,参考字段说明                |
| startTime | int64  | 否    | 开始时间,单位：毫秒                 |
| endTime | int64  | 否    | 结束时间,单位：毫秒                 |
| limit | int64  | 否    | 默认值:500 最大值:1440     |

 - 如果未发送 startTime 和 endTime ，默认返回最新的k线数据

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

**响应**

| 参数名    | 类型  | 描述 |
|--------|----|----|
| open   | float64 | 开盘价 |
| close  | float64 | 收盘价 |
| high   | float64 | 最高价 |
| low    | float64 | 最低价 |
| volume | float64 | 交易数量 |
| time   | int64 | k线时间戳，单位毫秒 |

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

如果未发送 startTime 和 endTime,默认返回最新的k线数据

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


## 8. 获取合约未平仓数

**HTTP请求**

```
    GET /openApi/swap/v2/quote/openInterest
```

**参数**

| 参数名  | 类型     | 是否必填 | 描述 |
| -------|--------|-----|------|
| symbol | string | 是  | 交易对, 例如: BTC-USDT, 请使用大写字母 |

**响应**

| 参数名          | 类型     | 描述   |
|--------------|--------|------|
| openInterest | string | 持仓数量 |
| symbol       | string | 合约名称 |
| time         | int64  | 撮合引擎时间 |

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

## 9. 24小时价格变动情况

**HTTP请求**

```
    GET /openApi/swap/v2/quote/ticker
```

**请求参数**

| 参数名  | 类型     | 是否必填 | 描述 |
| -------|--------|----|------|
| symbol | string | 否  | 交易对, 例如: BTC-USDT, 请使用大写字母 |

 - 不发送交易对参数，则会返回所有交易对信息

**响应**

| 参数名                | 类型     | 描述              |
|--------------------|--------|-----------------|
| symbol             | string | 交易对, 例如: BTC-USDT   |
| priceChange        | string | 24小时价格变动        |
| priceChangePercent | string | 价格变动百分比         |
| lastPrice          | string | 最新交易价格          |
| lastQty            | string | 最新交易额           |
| highPrice          | string | 24小时最高价         |
| lowPrice           | string | 24小时最低价         |
| volume             | string | 24小时成交量         |
| quoteVolume        | string | 24小时成交额, 单位是USDT |
| openPrice          | string | 24小时内第一个价格      |
| openTime           | int64  | 24小时内，第一笔交易的发生时间      |
| closeTime          | int64  | 24小时内，最后一笔交易的发生时间      |

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

或(当不发送交易对信息)
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


# 账户接口

## 1. 查询账户信息

- 查询当前账户下永续合约资产的相关信息。

**HTTP请求**
             
```
    GET /openApi/swap/v2/user/balance
```

**参数**

| 参数名 | 类型  | 是否必填 |描述 |
| ------------- |----|---|---- |
| timestamp | int64 | 是    | 请求的时间戳，单位为毫秒 |
| recvWindow       | int64 | 否    | 请求有效时间空窗值, 单位:毫秒    |

**响应**

| 参数名 | 类型     | 描述 |
| ------------- |--------|----|
| code           | int64  | 错误码，0表示成功，不为0表示异常失败 |
| msg            | string | 错误信息提示 |
| asset       | string | 用户资产 |
| balance        | string | 资产余额 |
| equity         | string | 资产净值 |
| unrealizedProfit  | string | 未实现盈亏 |
| realisedProfit    | string | 已实现盈亏 |
| availableMargin| string | 可用保证金 |
| usedMargin     | string | 已用保证金 |
| freezedMargin  | string | 冻结保证金 |

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

## 2. 查询持仓信息

- 查询当前账户下永续合约的持仓信息与盈亏情况。

**HTTP请求**

```
    GET /openApi/swap/v2/user/positions
```

**参数**  

| 参数名 | 类型     | 是否必填 | 描述|
| ------------- |--------|------|----|
| symbol | string | 否    |  交易对, 例如: BTC-USDT, 请使用大写字母 |
| timestamp | int64  | 是    | 请求的时间戳，单位为毫秒 |
| recvWindow       | int64  | 否    | 请求有效时间空窗值, 单位:毫秒    |

**响应**

| 参数名              | 类型     | 描述                          |
|------------------|--------|-----------------------------|
| symbol           | string | 交易对, 例如: BTC-USDT           |
| positionId       | string | 仓位ID                        |
| positionSide     | string | 仓位方向 LONG/SHORT 多/空         |
| isolated         | bool   | 是否是逐仓模式, true:逐仓模式 false:全仓 |
| positionAmt      | string | 持仓数量                        |
| availableAmt     | string | 可平仓数量                       |
| unrealizedProfit | string | 未实现盈亏                       |
| realisedProfit   | string | 已实现盈亏                       |
| initialMargin    | string | 保证金                         |
| avgPrice         | string | 开仓均价                        |
| leverage         | int    | 杠杆                          |

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

# 交易接口

## 1. 交易下单

- 当前账户在指定symbol合约上进行下单操作。(支持限价单、市价单、计划委托市价单、计划委托限价单、仓位止盈止损单、针对仓位平仓)

**HTTP请求**

```
    POST /openApi/swap/v2/trade/order
```

**参数**

| 参数名              | 类型      | 是否必填 | 描述                                                                                 |
|------------------|---------|------|------------------------------------------------------------------------------------|
| symbol           | string  | 是    | 交易对, 例如: BTC-USDT, 请使用大写字母                                                         |
| type             | string  | 是    | 订单类型 LIMIT, MARKET, STOP_MARKET, TAKE_PROFIT_MARKET, TRIGGER_LIMIT, TRIGGER_MARKET |
| side             | string  | 是    | 买卖方向 SELL, BUY                                                                     |
| positionSide     | string  | 否    | 持仓方向，且仅可选择 LONG 或 SHORT，默认LONG                                                     |
| price            | float64 | 否    | 委托价格                                                                               |
| quantity         | float64 | 否    | 下单数量,使用closePosition不支持此参数。                                                        |
| stopPrice        | float64 | 否    | 触发价, 仅 STOP_MARKET,TAKE_PROFIT_MARKET,TRIGGER_LIMIT,TRIGGER_MARKET 需要此参数 |
| timestamp        | int64   | 是    | 请求的时间戳，单位:毫秒                                                                       |
| recvWindow       | int64   | 否    | 请求有效时间空窗值, 单位:毫秒                                                                   |

基于订单 type 不同，强制要求某些参数:

| 类型                                | 强制要求的参数             |
|-----------------------------------|---------------------|
| LIMIT                             | quantity, price     |
| MARKET                            | quantity            |
| TRIGGER_LIMIT              | quantity、stopPrice、price |
| STOP_MARKET, TAKE_PROFIT_MARKET, TRIGGER_MARKET | quantity、stopPrice  |

- 条件单的触发必须:

  - STOP_MARKET 止损单:
    - 已挂止损单累加数量不能大于持仓数量
    - 买入: 标记价格高于等于触发价stopPrice
    - 卖出: 标记价格低于等于触发价stopPrice
  - TAKE_PROFIT_MARKET 止盈单:
    - 已挂止盈单累加数量不能大于持仓数量
    - 买入: 标记价格低于等于触发价stopPrice
    - 卖出: 标记价格高于等于触发价stopPrice

**响应**

| 参数名         | 类型      | 描述                                                         |
|-------------|---------|------------------------------------------------------------|
| symbol          | string  | 交易对, 例如: BTC-USDT            |
| side          | string  | 买卖方向    |
| type          | string  | 订单类型 |
| positionSide        | string  | 持仓方向         |
| orderId       | int64   | 订单号                     |


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

## 2. 批量下单

- 当前账户在指定symbol合约上进行批量下单操作。

**HTTP请求**

```
    POST /openApi/swap/v2/trade/batchOrders
```

**参数**


| 参数名              | 类型        | 是否必填 | 描述                      |
|------------------|-----------|------|-------------------------|
| batchOrders           | LIST\<Order> | 是    | 订单列表，最多支持5个订单,Order对象参考如下 |
| timestamp        | int64     | 是    | 请求的时间戳，单位:毫秒            |
| recvWindow       | int64     | 否    | 请求有效时间空窗值, 单位:毫秒        |

Order对象：

| 参数名              | 类型      | 是否必填 | 描述                                                                         |
|------------------|---------|------|----------------------------------------------------------------------------|
| symbol           | string  | 是    | 交易品种, 例如: BTC-USDT, 请使用大写字母                                                |
| type             | string  | 是    | 订单类型 LIMIT, MARKET, STOP_MARKET, TAKE_PROFIT_MARKET, TRIGGER_LIMIT, TRIGGER_MARKET        |
| side             | string  | 是    | 交易方向, (BUY/SELL 买/卖)                                                       |
| positionSide     | string  | 否    | 持仓方向，且仅可选择 LONG 或 SHORT，默认LONG                                                     |
| price            | float64 | 否    | 委托价格                                                                       |
| quantity         | float64 | 否    | 下单数量,使用closePosition不支持此参数。                                                |
| closePosition    | string  | 否    | true, false；触发后全部平仓，仅支持STOP_MARKET和TAKE_PROFIT_MARKET；不与quantity合用；自带只平仓效果 |
| stopPrice        | float64 | 否    | 止盈止损，计划委托，触发价, 仅 STOP_MARKET, TAKE_PROFIT_MARKET,TRIGGER                   |

- 具体订单条件规则，与普通下单一致
- 批量下单采取并发处理，不保证订单撮合顺序

**响应**

| 参数名         | 类型      | 描述                                                         |
|-------------|---------|------------------------------------------------------------|
| symbol          | string  | 交易对, 例如: BTC-USDT            |
| side          | string  | 买卖方向    |
| type          | string  | 订单类型 |
| positionSide        | string  | 持仓方向         |
| orderId       | int64   | 订单号                     |

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


## 3. 全部一键平仓下单

- 将当前账户下所有仓位进行一键平仓操作。注意，一键平仓是以市价委托进行触发的。

**HTTP请求**

```
    POST /openApi/swap/v2/trade/closeAllPositions
```

**参数**

| 参数名 | 类型  | 是否必填 | 描述             |
| ------------- |-----|------|----------------|
| timestamp | int64 | 是    | 请求的时间戳，单位:毫秒   |
| recvWindow | int64 | 否    | 请求有效时间空窗值, 单位:毫秒 |

**响应**

| 参数名 | 类型           | 描述                  |
| ---- |--------------|---------------------|
| success     | LIST\<int64> | 全部一键平仓产生的多个委托订单号    |
| failed     | 结构数组         | 平仓失败的订单号            |

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

## 4. 撤销订单

- 将当前账户处于当前委托状态的某个订单进行撤销操作。

**HTTP请求**
 
```
    DELETE /openApi/swap/v2/trade/order
```

**参数**

| 参数名 | 类型     | 是否必填 | 描述                        |
| ------------- |--------|------|---------------------------|
| orderId   | int64  | 是    | 订单号                       |
| symbol    | string | 是    | 交易对, 例如: BTC-USDT, 请使用大写字母 |
| timestamp | int64  | 是    | 请求的时间戳，单位:毫秒              |
| recvWindow | int64  | 否    | 请求有效时间空窗值, 单位:毫秒          |

**响应**

| 参数名         | 类型      | 描述                                                         |
|-------------|---------|------------------------------------------------------------|
| time          | int64   | 订单时间,单位:毫秒             |
| symbol          | string  | 交易对, 例如: BTC-USDT            |
| side          | string  | 买卖方向    |
| type          | string  | 订单类型 |
| positionSide        | string  | 持仓方向         |
| cumQuote        | string  | 成交金额                    |
| status        | string  | 订单状态                    |
| stopPrice        | string | 触发价            |
| price         | string | 委托价格                    |
| origQty | string | 原始委托数量                  |
| avgPrice      | string | 平均成交价                    |
| executedQty   | string | 成交量                     |
| orderId       | int64   | 订单号                     |
| profit        | string | 盈亏                      |
| commission    | string | 手续费                     |
| updateTime    | int64   | 更新时间,单位:毫秒            |

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

## 5. 批量撤销订单

- 将当前账户处于当前委托状态的部分订单进行批量撤销操作。

**HTTP请求**

```
    DELETE /openApi/swap/v2/trade/batchOrders
```

**参数**

| 参数名 | 类型           | 是否必填 | 描述                               |
| ------------- |--------------|------|----------------------------------|
| symbol    | string       | 是    | 交易对, 例如: BTC-USDT, 请使用大写字母       |
| orderIdList      | LIST\<int64> | 是    | 系统订单号, 最多支持10个订单[1234567,2345678] |
| timestamp | int64        | 是    | 请求的时间戳，单位：毫秒                     |
| recvWindow | int64        | 否    | 请求有效时间空窗值, 单位:毫秒                 |

**响应**

| 参数名 | 类型           | 描述              |
| ---- |--------------|-----------------|
| code          | int64        | 错误码，0表示成功，不为0表示异常失败 |
| msg           | string       | 错误信息提示          |
| success       | LIST\<order> | 撤销成功的订单列表       |
| failed        | 结构数组         | 撤销失败的订单列表       |
| orderId       | int64        | 订单号             |
| errorCode     | int64        | 错误码，0表示成功，不为0表示异常失败 |
| errorMessage  | string       | 错误信息提示          |

- order对象如下

| 参数名         | 类型      | 描述                                                         |
|-------------|---------|------------------------------------------------------------|
| time          | int64   | 订单时间,单位:毫秒             |
| symbol          | string  | 交易对, 例如: BTC-USDT            |
| side          | string  | 买卖方向    |
| type          | string  | 订单类型 |
| positionSide        | string  | 持仓方向         |
| cumQuote        | string  | 成交金额                    |
| status        | string  | 订单状态                    |
| stopPrice        | string | 触发价            |
| price         | string | 委托价格                    |
| origQty | string | 原始委托数量                  |
| avgPrice      | string | 平均成交价                    |
| executedQty   | string | 成交量                     |
| orderId       | int64   | 订单号                     |
| profit        | string | 盈亏                      |
| commission    | string | 手续费                     |
| updateTime    | int64   | 更新时间,单位:毫秒            |

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

## 6. 撤销全部订单

- 将当前账户处于当前委托状态的全部订单进行撤销操作。

**HTTP请求**

```
    DELETE /openApi/swap/v2/trade/allOpenOrders
```

**参数**

| 参数名        | 类型     | 是否必填 | 描述                        |
|------------|--------|-----|---------------------------|
| symbol     | string | 是   | 交易对, 例如: BTC-USDT, 请使用大写字母 |
| timestamp  | int64  | 是   | 请求的时间戳，单位:毫秒              |
| recvWindow | int64  | 否   | 请求有效时间空窗值, 单位:毫秒          |

**响应**

| 参数名          | 类型           | 描述              |
|--------------|--------------|-----------------|
| success      | LIST\<order> | 撤销成功的订单列表       |
| failed       | 结构数组       | 撤销失败的订单列表       |
| orderId      | int64        | 订单号             |
| errorCode    | int64        | 错误码，0表示成功，不为0表示异常失败 |
| errorMessage | string       | 错误信息提示          |

- order对象如下

| 参数名         | 类型      | 描述                                                         |
|-------------|---------|------------------------------------------------------------|
| time          | int64   | 订单时间,单位:毫秒             |
| symbol          | string  | 交易对, 例如: BTC-USDT            |
| side          | string  | 买卖方向    |
| type          | string  | 订单类型 |
| positionSide        | string  | 持仓方向         |
| cumQuote        | string  | 成交金额                    |
| status        | string  | 订单状态                    |
| stopPrice        | string | 触发价            |
| price         | string | 委托价格                    |
| origQty | string | 原始委托数量                  |
| avgPrice      | string | 平均成交价                    |
| executedQty   | string | 成交量                     |
| orderId       | int64   | 订单号                     |
| profit        | string | 盈亏                      |
| commission    | string | 手续费                     |
| updateTime    | int64   | 更新时间,单位:毫秒            |

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

## 7. 查询当前全部挂单

- 查询用户当前处于委托状态的全部订单。

**HTTP请求**

```
    GET /openApi/swap/v2/trade/openOrders
```

**参数**  

| 参数名     | 类型     | 是否必填 | 字段描述                      
|---------|--------|------|---------------------------|
| symbol  | string | 否    | 交易对, 例如: BTC-USDT, 请使用大写字母 |
| timestamp | int64  | 是    | 请求的时间戳，单位:毫秒              |
| recvWindow | int64  | 否    | 请求有效时间空窗值, 单位:毫秒          |

- 不带symbol参数，会返回所有交易对的挂单

 **响应**

| 参数名         | 类型      | 描述                                                         |
|-------------|---------|------------------------------------------------------------|
| time          | int64   | 订单时间,单位:毫秒             |
| symbol          | string  | 交易对, 例如: BTC-USDT            |
| side          | string  | 买卖方向    |
| type          | string  | 订单类型 |
| positionSide        | string  | 持仓方向         |
| cumQuote        | string  | 成交金额                    |
| status        | string  | 订单状态                    |
| stopPrice        | string | 触发价            |
| price         | string | 委托价格                    |
| origQty | string | 原始委托数量                  |
| avgPrice      | string | 平均成交价                    |
| executedQty   | string | 成交量                     |
| orderId       | int64   | 订单号                     |
| profit        | string | 盈亏                      |
| commission    | string | 手续费                     |
| updateTime    | int64   | 更新时间,单位:毫秒            |

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

## 8. 查询订单

- 查询订单详情
   
**HTTP请求**

```
    GET /openApi/swap/v2/trade/order
```

**参数**

| 参数名 | 类型     | 是否必填 | 描述                       |
| ------------- |--------|------|--------------------------|
| symbol | string | 是    | 交易对, 例如: BTC-USDT, 请使用大写字母 |
| orderId | int64  | 是    | 订单号                      |
| timestamp | int64  | 是    | 请求的时间戳，单位:毫秒             |
| recvWindow | int64  | 否    | 请求有效时间空窗值, 单位:毫秒         |

**响应**

| 参数名         | 类型      | 描述                                                         |
|-------------|---------|------------------------------------------------------------|
| time          | int64   | 订单时间,单位:毫秒             |
| symbol          | string  | 交易对, 例如: BTC-USDT            |
| side          | string  | 买卖方向    |
| type          | string  | 订单类型 |
| positionSide        | string  | 持仓方向         |
| cumQuote        | string  | 成交金额                    |
| status        | string  | 订单状态                    |
| stopPrice        | string | 触发价            |
| price         | string | 委托价格                    |
| origQty | string | 原始委托数量                  |
| avgPrice      | string | 平均成交价                    |
| executedQty   | string | 成交量                     |
| orderId       | int64   | 订单号                     |
| profit        | string | 盈亏                      |
| commission    | string | 手续费                     |
| updateTime    | int64   | 更新时间,单位:毫秒            |


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

## 9. 查询逐全仓模式

- 查询用户在指定symbol合约上的保证金模式：逐仓或全仓。

**HTTP请求**

```
    GET /openApi/swap/v2/trade/marginType
```

**参数**

| 参数名 | 类型     | 是否必填 | 描述                                |
| ------------- |--------|------|-----------------------------------|
| symbol      | string | 是    | 交易对, 例如: BTC-USDT, 请使用大写字母       |
| timestamp   | int64  | 是    | 请求时间戳, 单位:毫秒                    |
| recvWindow   | int64  | 否    | 请求有效时间空窗值, 单位:毫秒                  |

**响应**

| 参数名       | 类型     | 描述     |
| ----------- |--------| -------- |
| marginType  | string | 保证金模式 |

**备注**

| marginType | 字段说明 |
| ----------|----|
| ISOLATED | 逐仓 |
| CROSSED    | 全仓 |

```javascript
{
  "code": 0,
    "msg": "",
    "data": {
      "marginType": "CROSSED"
    }
}
```

## 10. 变换逐全仓模式

- 变换用户在指定symbol合约上的保证金模式：逐仓或全仓。

**HTTP请求**

```
    POST /openApi/swap/v2/trade/marginType
```

**参数**

| 参数名 | 类型     | 是否必填 | 描述                                |
| ------------- |--------|------|-----------------------------------|
| symbol      | string | 是    | 交易对, 例如: BTC-USDT, 请使用大写字母       |
| marginType  | string | 是    | 保证金模式 ISOLATED(逐仓), CROSSED(全仓) |
| timestamp   | int64  | 是    | 请求时间戳, 单位:毫秒                    |
| recvWindow   | int64  | 否    | 请求有效时间空窗值, 单位:毫秒                  |

**响应**

| 参数名 | 类型     | 描述 |
| ---- |--------| ---- |
| code | int64  | 错误码，0表示成功，不为0表示异常失败 |
| msg  | string | 错误信息提示 |


```javascript
{
  "code": 0,
  "msg": ""
}
```

## 11. 查询开仓杠杆

- 查询用户在指定symbol合约的开仓杠杆。

**HTTP请求**

```
    GET /openApi/swap/v2/trade/leverage
```

**参数**

| 参数名     | 类型     | 是否必填 | 描述                        |
| --------- |--------|------|---------------------------|
| symbol    | string | 是    | 交易对, 例如: BTC-USDT, 请使用大写字母 |
| timestamp | int64  | 是    | 请求的时间戳，单位:毫秒              |
| recvWindow | int64  | 否    | 请求有效时间空窗值，单位:毫秒           |

**响应**

| 参数名         | 类型    | 描述       |
| ------------- |-------| ---------- |
| longLeverage  | int64 | 多仓杠杆倍数 |
| shortLeverage | int64 | 空仓杠杆倍数 |

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

## 12. 调整开仓杠杆

- 调整用户在指定symbol合约的开仓杠杆。

**HTTP请求**

```
    POST /openApi/swap/v2/trade/leverage
```

**参数**

| 参数名 | 类型     | 是否必填 | 描述                         |
| ------------- |--------|------|----------------------------|
| symbol    | string | 是    | 交易对, 例如: BTC-USDT, 请使用大写字母 |
| side      | string | 是    | 多仓或者空仓的杠杆，LONG表示多仓，SHORT表示空仓 |
| leverage  | int64  | 是    | 杠杆倍数                       |
| timestamp | int64  | 是    | 请求的时间戳，单位:毫秒               |
| recvWindow | int64  | 否    | 请求有效时间空窗值，单位:毫秒            |

**响应**

| 参数名      | 类型     | 描述  |
|----------|--------|-----|
| leverage | int64  | 杠杆倍数 |
| symbol   | string | 交易对 |

```javascript
{
    "code": 0,
    "msg": "",
    "data": {
        "leverage": 6,
        "symbol": "BTC-USDT" // 交易对
    }
}
```

## 13. 用户强平单历史

- 查询用户强平单。

**HTTP请求**

```
    GET /openApi/swap/v2/trade/forceOrders
```

**参数**

| 参数名           | 类型   | 是否必填 | 描述                              |
|---------------|------|------|---------------------------------|
| symbol        | string | 否    | 交易对, 例如: BTC-USDT, 请使用大写字母      |
| autoCloseType | string | 否    | "LIQUIDATION":强平单, "ADL":ADL减仓单 |
| startTime   | int64 | 否    | 开始时间，单位:毫秒                      |
| endTime   | int64 | 否    | 结束时间，单位:毫秒                      |
| limit         | int  | 否    | 返回的结果集数量 默认值50，最大值100           |
| timestamp     | int64 | 是    | 发起请求的时间戳，单位:毫秒                  |
| recvWindow     | int64 | 否    | 请求有效时间空窗值，单位:毫秒                 |

 - 如果没有传 "autoCloseType", 强平单和ADL减仓单都会被返回
 - 如果没有传"startTime", 只会返回"endTime"之前7天内的数据

**响应**

| 参数名         | 类型      | 描述                                                         |
|-------------|---------|------------------------------------------------------------|
| time          | int64   | 订单时间,单位:毫秒             |
| symbol          | string  | 交易对, 例如: BTC-USDT            |
| side          | string  | 买卖方向    |
| type          | string  | 订单类型 |
| positionSide        | string  | 持仓方向         |
| cumQuote        | string  | 成交金额                    |
| status        | string  | 订单状态                    |
| stopPrice        | string | 触发价            |
| price         | string | 委托价格                    |
| origQty | string | 原始委托数量                  |
| avgPrice      | string | 平均成交价                    |
| executedQty   | string | 成交量                     |
| orderId       | int64   | 订单号                     |
| profit        | string | 盈亏                      |
| commission    | string | 手续费                     |
| updateTime    | int64   | 更新时间,单位:毫秒            |


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

## 14. 查询历史订单

- 查询用户历史订单(订单状态为已成交或已撤销)。

**HTTP请求**

```
    GET /openApi/swap/v2/trade/allOrders
```

**参数**

| 参数名        | 类型     | 是否必填 | 描述                    |
|------------|--------|------|-----------------------|
| symbol     | string | 是    | 交易对, 例如: BTC-USDT, 请使用大写字母 |
| orderId    | int64  | 否    | 只返回此orderID及之后的订单，缺省返回最近的订单 |
| startTime   | int64  | 否    | 开始时间，单位:毫秒            |
| endTime   | int64  | 否    | 结束时间，单位:毫秒            |
| limit      | int64  | 是    | 返回的结果集数量 默认值:500 最大值:1000 |
| timestamp  | int64  | 是    | 请求的时间戳，单位:毫秒          |
| recvWindow | int64  | 否    | 发起请求的时间戳，单位:毫秒        |

- 查询时间范围最大不得超过7天
- 默认查询最近7天内的数据

**响应**

| 参数名         | 类型      | 描述                                                         |
|-------------|---------|------------------------------------------------------------|
| time          | int64   | 订单时间,单位:毫秒             |
| symbol          | string  | 交易对, 例如: BTC-USDT            |
| side          | string  | 买卖方向    |
| type          | string  | 订单类型 |
| positionSide        | string  | 持仓方向         |
| cumQuote        | string  | 成交金额                    |
| status        | string  | 订单状态                    |
| stopPrice        | string | 触发价            |
| price         | string | 委托价格                    |
| origQty | string | 原始委托数量                  |
| avgPrice      | string | 平均成交价                    |
| executedQty   | string | 成交量                     |
| orderId       | int64   | 订单号                     |
| profit        | string | 盈亏                      |
| commission    | string | 手续费                     |
| updateTime    | int64   | 更新时间,单位:毫秒            |

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

## 15. 调整逐仓保证金

- 针对逐仓模式下的仓位，调整其逐仓保证金资金。

**HTTP请求**

```
    POST /openApi/swap/v2/trade/positionMargin
```

**参数**

| 参数名        | 类型      | 是否必填 | 描述                           |
|------------|---------|------|------------------------------|
| symbol     | string  | 是    | 交易对, 例如: BTC-USDT, 请使用大写字母  |
| amount     | float64 | 是    | 保证金资金                        |
| type       | int     | 是    | 调整方向 1: 增加逐仓保证金，2: 减少逐仓保证金   |
| positionSide      | string  | 否    | 持仓方向，且仅可选择 LONG 或 SHORT,默认LONG |
| timestamp  | int64   | 是    | 请求的时间戳，单位:毫秒                 |
| recvWindow | int64   | 否    | 请求有效时间窗口，单位:毫秒               |

**响应**

| 参数名             | 类型      | 描述                  |
|-----------------|---------|---------------------|
| code            | int64   | 错误码，0表示成功，不为0表示异常失败 |
| msg             | string  | 错误信息提示              |
| amount          | float64 | 保证金资金               |
| type            | int     | 调整方向 1: 增加逐仓保证金，2: 减少逐仓保证金                |

```javascript
{
    "code": 0,
    "msg": "",
    "amount": 1,
    "type": 1
}
```