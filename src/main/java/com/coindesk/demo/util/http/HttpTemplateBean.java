package com.coindesk.demo.util.http;

public interface HttpTemplateBean<RB, RP> {

    HttpTemplate<RB, RP> convertToHttpTemplate(String domain);

}
