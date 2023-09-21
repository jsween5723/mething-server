package com.esc.bluespring.common.ncp;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.hc.client5.http.utils.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
class NcpHeaderFactory {

  @Value("${naver-cloud.sms.access-key}")
  private String accessKey;

  @Value("${naver-cloud.sms.secret-key}")
  private String secretKey;

  NcpHeader generateHeader(HttpMethod method, String url) {
    return new NcpHeader(method, url);
  }

  class NcpHeader extends HttpHeaders {
    private final Long time = System.currentTimeMillis();

    NcpHeader(HttpMethod method, String url) {
      super();
      setContentType(MediaType.APPLICATION_JSON);
      set("x-ncp-apigw-timestamp", time.toString());
      set("x-ncp-iam-access-key", accessKey);
      set("x-ncp-apigw-signature-v2", makeSignature(method, url));
    }

    private String makeSignature(HttpMethod method, String url) {
      String timestamp = time.toString();
      String message = method.name() + ' ' + url + '\n' + timestamp + '\n' + accessKey;
      byte[] rawHmac = getMac().doFinal(message.getBytes(StandardCharsets.UTF_8));
      return Base64.encodeBase64String(rawHmac);
    }

    private Mac getMac() {
      SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8),
          "HmacSHA256");
      Mac mac;
      try {
        mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);
      } catch (NoSuchAlgorithmException | InvalidKeyException e) {
        throw new RuntimeException(e);
      }
      return mac;
    }
  }
}
