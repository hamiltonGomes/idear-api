package com.unicap.idear.idear.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ChatGPTService {

    @Value("${chatgpt.api.url}")
    private String chatGPTApiUrl;

    @Value("${chatgpt.api.key}")
    private String chatGPTApiKey;

    @Value("${chatgpt.api.model}")
    private String chatGPTApiModel;

    public String getChatGPTResponse(String problemTitle, String problemDescription, String ideaContent) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + chatGPTApiKey);

            String prompt = "Auxilie um grupo em um processo de ideação. " +
                    "A descrição do problema: " + problemTitle + " - " + problemDescription + ". " +
                    "A ideia mais votada: " + ideaContent + ". " +
                    "Não seja prolixo ou redundante." +
                    "Com base nos dados fornecidos, sintetize e relacione, de maneira analítica e objetiva, o problema inicial e a ideia mais votada." +
                    "LIMITE DE 800 CARACTERES EM APENAS UM PARÁGRAFO.";

            String requestBody = "{ \"model\": \"" + chatGPTApiModel + "\", \"messages\": [ { \"role\": \"user\", \"content\": \"" + prompt + "\" } ] }";

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(chatGPTApiUrl);

            builder.queryParam("model", chatGPTApiModel);

            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> responseEntity = new RestTemplate()
                    .exchange(builder.toUriString(), HttpMethod.POST, requestEntity, String.class);

            String responseBody = responseEntity.getBody();
            String contentValue = extractContentFromResponse(responseBody);

            return contentValue;

        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing request to ChatGPT";
        }
    }

    private String extractContentFromResponse(String responseBody) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseNode = objectMapper.readTree(responseBody);
            JsonNode contentNode = responseNode.path("choices").path(0).path("message").path("content");
            return contentNode.asText();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error on extracting value from 'content' field of ChatGPT response.";
        }
    }
}
