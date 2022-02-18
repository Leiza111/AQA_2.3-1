package ru.netology.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.data.DataGenerator;
import ru.netology.data.RegistrationByCardInfo;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;


public class TestingForm {
    public static String generateDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    String planningDate = DataGenerator.generateDate(21);
    String RedevelopedDate = DataGenerator.generateDate(28);
    @Test
    void shouldSuccessfulCardOrder() {
        open("http://localhost:9999");

        RegistrationByCardInfo firstMeeting = DataGenerator.Registration.generateByCard("ru");
        Configuration.holdBrowserOpen = true;


          $("[data-test-id='city'] input").setValue(firstMeeting.getCity()); //+
        $("[placeholder='Дата встречи']").doubleClick().sendKeys(Keys.DELETE); //+
        $("[data-test-id='date'] input").setValue(planningDate); //+
        $("[data-test-id='name'] input").setValue(firstMeeting.getName()); //+
        $("[data-test-id='phone'] input").setValue(firstMeeting.getPhone()); //+
        $("[data-test-id='agreement']").click(); //+
        $$(".button__text").find(exactText("Запланировать")).click();

 //       $(".notification__content").shouldBe(exactText("Встреча успешно запланирована на " + RedevelopedDate));
 //       $(".notification__content").shouldHave(exactText("Встреча успешно запланирована на " + RedevelopedDate), Duration.ofSeconds(20));
        $("[data-test-id='success-notification'] .notification__content").shouldBe(Condition.visible)
                .shouldHave(Condition.text("Встреча успешно запланирована на  " + generateDate(3)), Duration.ofSeconds(15));
        $("button.notification__closer").click();
     //   $("[data-test-id='notification'] .notification__title").shouldBe(visible, Duration.ofSeconds(20)).shouldHave(exactText("Встреча успешно запланирована на " + RedevelopedDate));
//        $("[data-test-id='notification'] .notification__content").shouldBe(visible, Duration.ofSeconds(20)).shouldHave(exactText("Встреча успешно забронирована на " + planningDate));

        //перепланирование даты
        $("[placeholder='Дата встречи']").doubleClick().sendKeys(Keys.DELETE); //+
        $("[data-test-id='date'] input").setValue(RedevelopedDate);
        $$(".button__text").find(exactText("Запланировать")).click();
        $("[data-test-id='replan-notification'] .notification__content")
                .shouldBe(visible).shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"));

        $$(".button__text").find(exactText("Перепланировать")).click();

        $(".notification__content")
                .shouldBe(visible).shouldHave(exactText("Встреча успешно запланирована на " + RedevelopedDate), Duration.ofSeconds(15));
    }
}
