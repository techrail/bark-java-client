package bark.client.http;

import bark.client.constants.Global;
import bark.client.models.BarkLog;
import bark.client.models.Config;
import okhttp3.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class Network extends Config {
    private final OkHttpClient httpClient = new OkHttpClient();
    private String post(String url, String payLoad) throws IOException {

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(payLoad, MediaType.parse("application/json")))
                .build();
        try
                (Response response = httpClient.newCall(request).execute()){
            if(response.code()!=200) System.out.println("E#1LKACM - POST Request failed." + response.code() + "|" + response.body());
        }
        return "200 - OK";
    }
//        httpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//                System.out.println("E#1LKACM - POST Request failed.");
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                if (response.code() != 200) {
//                    System.out.println("E#1LKACM - POST request failed." + response.code() + "|" + response.message());
//                } else {
//                    System.out.println("200 - OK");
//                }
//
//
//            }
//        });
     //       return "Done";
   // }
        public String postSingleLog(BarkLog barkLog, String url) throws IOException {
         ObjectMapper mapper = new ObjectMapper();
         String rawJson = mapper.writeValueAsString(barkLog);
         return post(url,rawJson);
     }

    public String postMultipleLogs(List<BarkLog> barkLog, String url) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String rawJson = mapper.writeValueAsString(barkLog);
        return post(url,rawJson);
    }


}
