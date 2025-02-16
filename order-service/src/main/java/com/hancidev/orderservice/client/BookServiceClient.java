package com.hancidev.orderservice.client;

import com.hancidev.orderservice.dto.response.BookApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "${book-service.url}")
public interface BookServiceClient {

    @GetMapping("/find/{bookID}")
    BookApiResponse findBook(@PathVariable String bookID);
}
