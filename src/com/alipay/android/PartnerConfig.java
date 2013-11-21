package com.alipay.android;

public class PartnerConfig {

	// 合作商户ID。用签约支付宝账号登录ms.alipay.com后，在账户信息页面获取。
	public static final String PARTNER = "2088111071953813";
	// 商户收款的支付宝账号
	public static final String SELLER = "gzhzyz@126.com";
	// 商户（RSA）私钥
	public static final String RSA_PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBANq+8qjeSTvJ3kf8cK31Enrp9rqF4xJp4AtxRVtf+odN+nyzHmmfoORvFzntBJSOuxh6yhhKDWQpoapVYNUm9LMNsgb8FlUg7X9gQfE6AdayH/hvnrHN9gYV3XirmitGwJDK8YdLgfirYWwS7D75cOkAH1AH5a3BUs2zDy1VEqvTAgMBAAECgYBDOOMBmi4PPZ4dqSpHi0S+mR/Felep1PeRDKIvznMDGaecor753qBnEuykVH5P2P0OLM3mYPt0iIax/cLgPjR8MHqfnLWG+H71bvZ7bSjLopKCWqXJO1avhMfhaRzpmcIT9Dwx9vHHQrwu7gRxsHKA/zm//wU/qOBoSGIFvXlUQQJBAPPTStUc5ZFuaZLT/4Nq6q6Rm85vojUIxT9P3J/62XXzHtk/Pl7gOb7oHPY9LgEjdlmRwRXWcAvVxYTY9I9LYGsCQQDlqxOCyMg0+CvYKJ0C9Fz/XBC0Zq+W5ODm7r6oWFuLM3eo/MtZshbtpnz/DLmOv2wE1XY5u9+ez6OMzajJ25w5AkEAk8dnXp1CApAiCqzU+6jMjnuQLGF6tZwYONRFiuz16kgtVUzAl65dMXjrPM8919DPhWAS4BJ1HKc3LNp193ee+wJBALm5F2UKgsQ68BGz/gx2mYGy7JtF8rZ+TdTRugIeuaCetSRFkRfuqRXzLV9+5csmxu49xIY5hENDvAlQAi+CVRkCQQDnBX6tiaj6VchKIgSVBDmMzcihuugPolofrg3MCcp8Zt4pRqWfldc9kBNHkHruR0lm53lGYTl8twO6IN1FP69U";
	// 支付宝（RSA）公钥 用签约支付宝账号登录ms.alipay.com后，在密钥管理页面获取。
	public static final String RSA_ALIPAY_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDQhhlgIMozot2PavQVOqERkHQQm/wC7QqijB8A 5O3j3GX4WV3CDAav6YIkpYqE514uMUINZdKLV29DtslzJwu2v/XtWq2j/Jk3zGJB1DKzg1EXdKmj Jee2RIwAtKlCMaQ+tqOzxqbDgBNquUbuu941LH1Uy3tcnuVItZs8FflrUQIDAQAB";
	// 支付宝安全支付服务apk的名称，必须与assets目录下的apk名称一致
	public static final String ALIPAY_PLUGIN_NAME = "alipay_plugin_20120428msp.apk";

}
