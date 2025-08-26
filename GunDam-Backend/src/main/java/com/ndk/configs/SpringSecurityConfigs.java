/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ndk.configs;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ndk.filters.JwtFilter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

/**
 *
 * @author Tran Quoc Phong
 */
@Configuration
@EnableWebSecurity
@EnableTransactionManagement
@ComponentScan(basePackages = {
    "com.ndk.controllers",
    "com.ndk.repositories",
    "com.ndk.services"
})
public class SpringSecurityConfigs {
    @Autowired
    private org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
        return new HandlerMappingIntrospector();
    }

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dp4fipzce",
                "api_key", "228386996632957",
                "api_secret", "k8HDLZbie2T8UWvC70S7f-SukGY",
                "secure", true));
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowCredentials(true);

        // QUAN TRỌNG: thêm allowedHeaders
        config.setAllowedHeaders(List.of("*"));
        // (hoặc liệt kê cụ thể: "Authorization","Content-Type","Accept","Origin","X-Requested-With","Accept-Language","Referer")

        config.setExposedHeaders(List.of("Authorization"));
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(
                org.springframework.security.config.http.SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth -> auth
                // 1) CHO PHÉP preflight
                .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                // 2) PUBLIC
                .requestMatchers(org.springframework.http.HttpMethod.GET,
                        "/api/phiendaugianguoidung/lich-su/**").permitAll() // ⬅ mở public cho lịch sử
                .requestMatchers(org.springframework.http.HttpMethod.GET,
                        "/api/taikhoannganhang/nguoiban/**").permitAll()
                .requestMatchers("/api/login/**", "/api/public/**").permitAll()
                .requestMatchers("/login", "/css/**", "/js/**").permitAll()
                // 3) CẦN ĐĂNG NHẬP
                .requestMatchers("/api/secure/**").authenticated()
                .requestMatchers("/api/mohinh/**").authenticated()
                .requestMatchers("/api/nguoidungcuocdaugia/**").authenticated()
                .requestMatchers("/api/cuocdaugia/bai-dau-gia/**").authenticated()
                .requestMatchers("/api/cuocdaugia/baidau/**").authenticated()
                .requestMatchers("/api/don/**").authenticated()
                .requestMatchers("/api/taikhoannganhang/**").authenticated()
                // 4) Còn lại trong /api cho phép (nếu bạn muốn GET công khai)
                .requestMatchers("/api/**").permitAll()
                // 5) Vai trò
                .requestMatchers("/admin/thongKeDauGia").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/user/**").hasAuthority("ROLE_NGUOIDUNG")
                .anyRequest().authenticated()
                )
                .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error=true")
                .permitAll()
                )
                .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
                );

        // Đăng ký filter JWT trước UsernamePasswordAuthenticationFilter
        http.addFilterBefore(new JwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
