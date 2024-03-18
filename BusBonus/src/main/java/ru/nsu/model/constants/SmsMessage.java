package ru.nsu.model.constants;

import lombok.Data;

@Data
public class SmsMessage {

    private String message;

    private String target; //Отправка конкретным адресатам (список телефонов через запятую):'89167777777,+79999999992'

    private String sender;

    private String username;

    private String password;

    private final String action = "post_sms";
    private String post_id;
    private Integer period;
    private String time_period;
    private Integer time_local;
    private String autotrimtext; //обрезать все пробелы и переводы строк в начале и конце сообщения.
}
