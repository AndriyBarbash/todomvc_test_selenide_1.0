package todomvc_test_selenide;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.CollectionCondition.empty;
import static com.codeborne.selenide.CollectionCondition.exactTexts;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static io.github.bonigarcia.wdm.WebDriverManager.chromedriver;

public class todomvc_Selenide_test {

    private static SelenideElement newTask = $("#new-todo");
    private static ElementsCollection tasks = $$("#todo-list>li");

    @Before
    public void openTodoMVC() {

        chromedriver().version("2.46").setup();
        open("http://todomvc.com/examples/emberjs/");
        newTask.shouldBe(visible, enabled);
    }

    @After
    public void clearData() {
        Selenide.executeJavaScript("localStorage.clear()");
    }

    @Test
    public void E2Etest_todomvc() {

        addTasks();
        assertTasks();
        edit();
        assertEdited();
        remove();
        toggle();
        assertItemsLeft();
        filterCompleted();
        filterActive();
        filterAll();
        toggleAll();
        clearCompleted();
        assertNoTasks();

    }

    private static void addTasks() {
        newTask.setValue("record A");
        newTask.pressEnter();
        newTask.shouldBe(visible, enabled);
        newTask.setValue("record B").pressEnter().shouldBe(visible, enabled);
        newTask.setValue("record C").pressEnter();
    }

    private void assertTasks() {
        tasks.shouldHave(exactTexts("record A", "record B", "record C"));
    }

    private void edit() {
        tasks.last().find("label").doubleClick();
        tasks.last().$(".edit").sendKeys(Keys.chord(Keys.CONTROL, "a", Keys.DELETE), "edited record", Keys.ENTER);
    }

    private void assertEdited() {
        tasks.last().shouldHave(exactText("edited record"));

    }

    private void remove() {
        tasks.find(exactText("edited record")).hover().find(".destroy").shouldBe(visible, enabled).click();

    }

    private void toggle() {
        tasks.find(exactText("record A")).find(".toggle").click();
        tasks.find(exactText("record A")).find(".toggle").click();
        tasks.find(exactText("record B")).find(".toggle").click();

    }

    private void assertItemsLeft() {
        $("#todo-count>strong").shouldHave(exactText("1"));

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
