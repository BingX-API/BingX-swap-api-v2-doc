package main

import (
	"crypto/hmac"
	"crypto/sha256"
	"encoding/hex"
	"fmt"
	"io/ioutil"
	"net/http"
	"net/url"
	"time"
)

const (
	urlStr    = "https://open-api.bingx.com"
	apiKey    = "Set your api key here !!"
	secretKey = "Set your secret key here!!"
)

func computeHmac256(strMessage string, strSecret string) string {
	key := []byte(strSecret)
	h := hmac.New(sha256.New, key)
	h.Write([]byte(strMessage))

	return hex.EncodeToString(h.Sum(nil))
}

func send_request(requestUrl, method string) (responseData []byte, err error) {
	u, _ := url.Parse(requestUrl)
	req := http.Request{
		Method: method,
		URL:    u,
		Header: http.Header{},
	}
	req.Header.Set("X-BX-APIKEY", apiKey)
	cli := http.Client{
		Timeout: 3 * time.Second,
	}
	response, err := cli.Do(&req)
	if err != nil {

		return
	}
	defer response.Body.Close()

	responseData, err = ioutil.ReadAll(response.Body)
	return
}

func placeMarketOrder(symbol, side, quantity, positionSide string) {
	requestPath := "/openApi/swap/v2/trade/order"
	requestUrl := urlStr + requestPath
	timestamp := time.Now().UnixNano() / int64(time.Millisecond)
	mapParams := url.Values{}
	mapParams.Set("symbol", symbol)
	mapParams.Set("side", side)
	mapParams.Set("type", "MARKET")
	mapParams.Set("positionSide", positionSide)
	mapParams.Set("quantity", quantity)
	mapParams.Set("timestamp", fmt.Sprint(timestamp))
	strParams := mapParams.Encode()
	strParams += "&signature=" + computeHmac256(strParams, secretKey)
	requestUrl += "?"
	requestUrl += strParams
	responseData, err := send_request(requestUrl, http.MethodPost)
	fmt.Println("\trequest:", requestUrl)
	fmt.Println("\tresponse:", string(responseData), err)
}

func getBalance() {
	requestPath := "/openApi/swap/v2/user/balance"
	requestUrl := urlStr + requestPath
	timestamp := time.Now().UnixNano() / int64(time.Millisecond)
	mapParams := url.Values{}
	mapParams.Set("timestamp", fmt.Sprint(timestamp))
	strParams := mapParams.Encode()
	strParams += "&signature=" + computeHmac256(strParams, secretKey)
	requestUrl += "?"
	requestUrl += strParams
	responseData, err := send_request(requestUrl, http.MethodGet)
	fmt.Println("\trequest:", requestUrl)
	fmt.Println("\tresponse:", string(responseData), err)
}

func main() {
	fmt.Println("placeMarketOrder:")
	placeMarketOrder("LINK-USDT", "BUY", "5", "LONG")

	fmt.Println("getBalance:")
	getBalance()
}
