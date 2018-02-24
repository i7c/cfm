package org.rliz.cfm.recorder.common.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
class WebSecurityConfiguration : WebSecurityConfigurerAdapter(false) {

    override fun configure(http: HttpSecurity?) {
        http!!.authorizeRequests().anyRequest().authenticated().and().httpBasic().and().csrf().disable()
    }
}
