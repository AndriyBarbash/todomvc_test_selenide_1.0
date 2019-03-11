package todomvc_test_selenide;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class todomvc_Selenide_test {

    private static SelenideElement newTask = $("#new-todo");
    private static ElementsCollection tasks = $$("#todo-list>li");

    @Test
    public void E2Etest_todomvc() {

        openTodoMVC();
        add("record A", "record B", "record C");
        assertTasks("record A", "record B", "record C");
        edit("record C", "edited record");
        assertEdited("edited record");
        remove("edited record");
        toggle("record A", "record B");
        assertItemsLeft("1");
        filterCompleted();
        filterActive();
        filterAll();
        toggleAll();
        clearCompleted();
        assertNoTasks();

    }

    private void openTodoMVC() {
        open("http://todomvc.com/examples/emberjs/");
        newTask.shouldBe(visible, enabled);

    }

    private void add(String taskText1, String taskText2, String taskText3) {
        newTask.setValue(taskText1).pressEnter();
        newTask.shouldBe(visible, enabled);
        newTask.setValue(taskText2).pressEnter();
        newTask.shouldBe(visible, enabled);
        newTask.setValue(taskText3).pressEnter();
    }

    private void assertTasks(String taskText1, String taskText2, String taskText3) {
        tasks.shouldHave(exactTexts(taskText1, taskText2, taskText3));
    }

    private void edit(String taskText, String taskTextedited) {
        tasks.last().find("label").doubleClick();
        tasks.last().$(".edit").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE), taskTextedited, Keys.ENTER);
    }

    private void assertEdited(String taskTextedited) {
        tasks.last().shouldHave(exactText(taskTextedited));

    }

    private void remove(String taskTextedited) {
        tasks.find(exactText(taskTextedited)).hover().find(".destroy").shouldBe(visible, enabled).click();

    }

    private void toggle(String taskText1, String taskText2) {
        tasks.find(exactText(taskText1)).find(".toggle").click();
        tasks.find(exactText(taskText1)).find(".toggle").click();
        tasks.find(exactText(taskText2)).find(".toggle").click();

    }

    private void assertItemsLeft(String itemsLeft) {
        $("#todo-count>strong").shouldHave(exactText(itemsLeft));

    }

    private void filterCompleted() {
        $(By.linkText("Completed")).click();
        tasks.shouldHaveSize(1);

    }

    private void filterActive() {
        $(By.linkText("Active")).click();
        tasks.shouldHaveSize(1);
    }

    private void filterAll() {
        $(By.linkText("All")).click();
        tasks.shouldHaveSize(2);

    }

    private void toggleAll() {
        $("#toggle-all").click();
        $("#todo-count>strong").shouldHave(exactText("0"));
    }

    private void clearCompleted() {
        $("#clear-completed").click();
        $("#clear-completed").shouldBe(hidden);

    }

    private void assertNoTasks() {
        tasks.shouldBe(empty);

    }

}
