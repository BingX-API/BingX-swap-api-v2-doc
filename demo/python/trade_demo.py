import time
import requests
import hmac
from hashlib import sha256

APIURL = "https://open-api.bingx.com"
APIKEY = "Set your api key here !!"
SECRETKEY = "Set your secret key here!!"

def get_sign(api_secret, payload):
    signature = hmac.new(api_secret.encode("utf-8"), payload.encode("utf-8"), digestmod=sha256).hexdigest()
    print("sign=" + signature)
    return signature


def send_request(methed, path, urlpa, payload):
    url = "%s%s?%s&signature=%s" % (APIURL, path, urlpa, get_sign(SECRETKEY, urlpa))
    print(url)

    headers = {
        'X-BX-APIKEY': APIKEY,
    }

    response = requests.request(methed, url, headers=headers, data=payload)
    return response.text

def praseParam(paramsMap):
    sortedKeys = sorted(paramsMap)
    paramsStr = "&".join(["%s=%s" % (x, paramsMap[x]) for x in sortedKeys])
    return paramsStr

def cacel_order():
    payload = {}
    path = '/openApi/swap/v2/trade/order'
    methed = "DELETE"
    paramsMap = {
        "orderId": "1622560",
        "timestamp": int(time.time() * 1000),
        "symbol": "LINK-USDT",
    }
    paramsStr = praseParam(paramsMap)
    return send_request(methed, path, paramsStr, payload)

def get_balance():
    payload = {}
    path = '/openApi/swap/v2/user/balance'
    methed = "GET"
    paramsMap = {
        "timestamp": int(time.time() * 1000),
    }
    paramsStr = praseParam(paramsMap)
    return send_request(methed, path, paramsStr, payload)

def post_market_order():
    payload = {}
    path = '/openApi/swap/v2/trade/order'
    methed = "POST"
    paramsMap = {
        "side": "BUY",
        "positionSide": "LONG",
        "quantity": 5,
        "symbol": "LINK-USDT",
        "type": "MARKET",
        "timestamp": int(time.time() * 1000),
    }
    paramsStr = praseParam(paramsMap)
    return send_request(methed, path, paramsStr, payload)

if __name__ == '__main__':

    print("cacelOrder:", cacel_order())

    print("getBalance:", get_balance())

    print("postMarketOrder:", post_market_order())
