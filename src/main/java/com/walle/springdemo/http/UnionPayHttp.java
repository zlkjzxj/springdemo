package com.walle.springdemo.http;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class UnionPayHttp {
    private String url;
    private static String merId = "777290058164754";

    public UnionPayHttp(String url) {
        this.url = url;
    }

    public String test(Map<String, String> data) {

        CloseableHttpClient client = HttpClients.createDefault();
        String result = "";
        try {
            HttpPost httpPost = new HttpPost(url);
        /*post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=" + encoding);
        post.addRequestHeader("cache-control", "no-cache");
        post.addRequestHeader("pragma", "no-cache");
        post.addRequestHeader("connection", "keep-alive");*/
            Header header = new BasicHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            Header header1 = new BasicHeader("cache-control", "no-cache");
            Header header2 = new BasicHeader("pragma", "no-cache");
            Header header3 = new BasicHeader("connection", "keep-alive");
            httpPost.setHeaders(new Header[]{header, header1, header2, header3});

            List<NameValuePair> list = new ArrayList<>();
            for (Entry<String, String> entry : data.entrySet()) {
                list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }

            HttpEntity httpEntity = new UrlEncodedFormEntity(list, "utf-8");

            httpPost.setEntity(httpEntity);
            CloseableHttpResponse response = client.execute(httpPost);
            if (response != null) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    result = EntityUtils.toString(entity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        //版本号，交易类型、子类，签名方法，签名值等关键域未上送，返回“Invalid request.”；
        String url = "https://gateway.test.95516.com/gateway/api/frontTransReq.do";
        Map<String, String> requestData = new HashMap<String, String>();


/***银联全渠道系统，产品参数，除了encoding自行选择外其他不需修改***/

//版本号，全渠道默认值

        requestData.put("version", "5.1.0");

        //字符集编码，可以使用UTF-8,GBK两种方式

        requestData.put("encoding", "utf-8");

        //签名方法

        requestData.put("signMethod", "01");

        //交易类型 ，01：消费

        requestData.put("txnType", "01");

        //交易子类型， 01：自助消费

        requestData.put("txnSubType", "01");

        //业务类型，B2C网关支付，手机wap支付

        requestData.put("bizType", "000201");

        //渠道类型，这个字段区分B2C网关支付和手机wap支付；07：PC,平板 08：手机

        requestData.put("channelType", "07");


        /***商户接入参数***/

        //商户号码，请改成自己申请的正式商户号或者open上注册得来的777测试商户号

        requestData.put("merId", merId);

        //接入类型，0：直连商户

        requestData.put("accessType", "0");

        //商户订单号，8-40位数字字母，不能含“-”或“_”，可以自行定制规则

        requestData.put("orderId", "20181121170051");

        //订单发送时间，取系统时间，格式为YYYYMMDDhhmmss，必须取当前时间，否则会报txnTime无效

        requestData.put("txnTime", "20181121170355");

        //交易币种（境内商户一般是156 人民币）

        requestData.put("currencyCode", "156");

        //交易金额，单位分，不要带小数点

        requestData.put("txnAmt", "1000");


        //前台通知地址 （需设置为外网能访问 http https均可），支付成功后的页面 点击“返回商户”按钮的时候将异步通知报文post到该地址

//如果想要实现过几秒中自动跳转回商户页面权限，需联系银联业务申请开通自动返回商户权限

//异步通知参数详见open.unionpay.com帮助中心 下载 产品接口规范 网关支付产品接口规范 消费交易 商户通知

        requestData.put("frontUrl", "http://localhost:8080/login");


        //后台通知地址（需设置为【外网】能访问 http https均可），支付成功后银联会自动将异步通知报文post到商户上送的该地址，失败的交易银联不会发送后台通知

//后台通知参数详见open.unionpay.com帮助中心 下载 产品接口规范 网关支付产品接口规范 消费交易 商户通知

//注意:1.需设置为外网能访问，否则收不到通知 2.http https均可 3.收单后台通知后需要10秒内返回http200或302状态码

// 4.如果银联通知服务器发送通知后10秒内未收到返回状态码或者应答码非http200，那么银联会间隔一段时间再次发送。总共发送5次，每次的间隔时间为0,1,2,4分钟。

// 5.后台通知地址如果上送了带有？的参数，例如：http://abc/web?a=b&c=d 在后台通知处理程序验证签名之前需要编写逻辑将这些字段去掉再验签，否则将会验签失败

        requestData.put("backUrl", "http://localhost:8080/login");

        String result = new UnionPayHttp(url).test(requestData);
        System.out.println(result);

    }
}
