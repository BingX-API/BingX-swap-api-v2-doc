<?php

$url = "https://open-api.bingx.com";
$apiKey = "Set your api key here!!";
$secretKey = "Set your secret key here!!";

function getOriginString(array $params) {
    // combine origin string
    $originString = "";
    $first = true;
    foreach($params as $n => $v) {
          if (!$first) {
              $originString .= "&";
          }
          $first = false;
          $originString .= $n . "=" . $v;
    }
    return $originString;
}

function getSignature(string $originString) {
    global $secretKey;
    $signature = hash_hmac('sha256', $originString, $secretKey, true);
    $signature = bin2hex($signature);
    return $signature;
}

function getRequestUrl(string $path, array $params) {
    global $url;
    $requestUrl = $url.$path."?";
    $first = true;
    foreach($params as $n => $v) {
          if (!$first) {
              $requestUrl .= "&";
          }
          $first = false;
          $requestUrl .= $n . "=" . $v;
    }
    return $requestUrl;
}

function httpPost($url)
{
    global $apiKey;
    $curl = curl_init($url);
    curl_setopt($curl, CURLOPT_POST, true);
    curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($curl, CURLOPT_USERAGENT, "curl/7.80.0");
    curl_setopt($curl, CURLOPT_HTTPHEADER, array(
        "X-BX-APIKEY:".$apiKey,
    ));
    $response = curl_exec($curl);
    curl_close($curl);
    return $response;
}

function placeMarketOrder(string $symbol, string $side, string $quantity, string $positionSide) {

    // interface info
    $path = "/openApi/swap/v2/trade/order";

    // interface params
    $params = array();
    $params['symbol'] = $symbol;
    $params['side'] = $side;
    $params['type'] = "MARKET";
    $params['quantity'] = $quantity;
    $params['positionSide'] = $positionSide;
    $date = new DateTime();
    $params['timestamp'] = $date->getTimestamp()*1000;

    // generate signature
    $originString = getOriginString($params);
    $signature = getSignature($originString);
    $params["signature"] = $signature;

    // send http request
    $requestUrl = getRequestUrl($path, $params);
    $result = httpPost($requestUrl);
    echo "\t";
    echo $result;
    echo "\n";
}

echo "placeMarketOrder:\n";
placeMarketOrder("LINK-USDT", "BUY", "5", "LONG");

?>