package com.adiDev.emailwriter;

import com.adiDev.emailwriter.controller.EmailRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class EmailGeneratorService {

    private final WebClient client;
    @Value("${gemini.api.key}")
    private  String gemeiniAPI;
    @Value("${gemini.api.url}")
    private String geminiURL;

    public EmailGeneratorService(WebClient.Builder client) {
        this.client = client.build();
    }
    //this will call api of gemini

    public String generatEmailReply(EmailRequest req){


        String prompt= buildPrompt(req);
        // build the prompt
        Map<String , Object> request= Map.of(
                "contents",new Object[]{
                        Map.of("parts",new Object[]{
                                Map.of("text",prompt)
                        })
                }
        );

        //String reponse= client.post().uri(geminiURL).header("Content-Type","application/json").retrieve().bodyToMono(String.class).block();
        String response= client.post().uri(geminiURL)
                .header("Content-Type","application/json")
                .header("x-goog-api-key",gemeiniAPI)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return extractContent(response);
        //craft request
        //send request
        //return response

    }

    private String extractContent(String reponse) {
        try {
            ObjectMapper mapper= new ObjectMapper();
            JsonNode roodtNode= mapper.readTree(reponse);
            return roodtNode.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();
        }catch (Exception e){
            return "Error processing request"+e.getMessage();
        }
    }
    private String buildPrompt(EmailRequest req) {
        StringBuilder prompt= new StringBuilder();
        prompt.append("Generate a professional Email reply for the following email content, please dont add the subject field");
        if(req.getTone()!=null && !req.getTone().isEmpty()){
            prompt.append("use a ").append(req.getTone()).append("tone");
        }
        prompt.append("\n Original email : \n ").append(req.getEmailContent());

        return prompt.toString();
    }
}
