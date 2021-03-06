package com.epam.jdi.uitests.testing.simple.examples;

import com.epam.commons.map.MapArray;
import com.epam.jdi.uitests.core.interfaces.complex.interfaces.ICell;
import com.epam.jdi.uitests.core.interfaces.complex.interfaces.ITable;
import com.epam.jdi.uitests.testing.TestsBase;
import com.epam.web.matcher.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static com.epam.jdi.site.epam.EpamSite.jobDescriptionPage;
import static com.epam.jdi.site.epam.EpamSite.jobListingPage;
import static com.epam.jdi.uitests.core.interfaces.complex.interfaces.Column.inColumn;
import static com.epam.jdi.uitests.core.interfaces.complex.interfaces.WithValue.withValue;


public class TableExamples extends TestsBase {
    private ITable jobsTable() {
        return jobListingPage.jobsList;
    }
    
    @BeforeMethod
    public void before(Method method) {
        jobListingPage.shouldBeOpened();
        Assert.isFalse(jobsTable()::isEmpty);
    }
    @Test
    public void getTableInfo() {
        jobListingPage.shouldBeOpened();
        Assert.isFalse(jobsTable()::isEmpty);
        Assert.areEquals(jobsTable().columns().count(), 4);
        Assert.areEquals(jobsTable().rows().count(), 5);
        Assert.areEquals(jobsTable().getValue(),
    "||X||name|category|location|apply||\n" +
            "||1||Senior Software Testing Engineer|Software Test Engineering|St-Petersburg, Russia|Apply||\n" +
            "||2||Software Test Automation Engineer (front-end)|Software Test Engineering|St-Petersburg, Russia|Apply||\n" +
            "||3||Test Automation Engineer (back-end)|Software Test Engineering|St-Petersburg, Russia|Apply||\n" +
            "||4||QA Specialist|Software Test Engineering|St-Petersburg, Russia|Apply||\n" +
            "||5||Testing Team Leader|Software Test Engineering|St-Petersburg, Russia|Apply||");
    }

    @Test
    public void searchInTable() {
        jobListingPage.shouldBeOpened();
        Assert.isFalse(jobsTable()::isEmpty);
        jobsTable()
            .row(withValue("QA Specialist"), inColumn("name"))
            .get("apply").select();

        jobDescriptionPage.checkOpened();
    }
    @Test
    public void searchContainsInTable() {
        jobListingPage.shouldBeOpened();
        Assert.isFalse(jobsTable()::isEmpty);
        jobsTable()
            .rowContains("Automation Engineer", inColumn("name"))
            .get("apply").select();

        jobDescriptionPage.checkOpened();
    }
    @Test
    public void searchMatchInTable() {
        jobListingPage.shouldBeOpened();
        Assert.isFalse(jobsTable()::isEmpty);
        jobsTable()
                .rowMatch(".+ Automation Engineer.*", inColumn("name"))
                .get("apply").select();

        jobDescriptionPage.checkOpened();
    }
    @Test
    public void searchContainsListInTable() {
        jobListingPage.shouldBeOpened();
        Assert.isFalse(jobsTable()::isEmpty);
        MapArray<String, ICell> firstRow = jobsTable().rows(
                "name~=Automation Engineer",
                "category*=.*Test Engineering")
                .first().value;

        Assert.areEquals(firstRow.get("name").getText(), "Software Test Automation Engineer (front-end)");
        Assert.areEquals(firstRow.get("category").getText(), "Software Test Engineering");
    }

    @Test
    public void searchByMultiCriteriaInTable() {
        jobListingPage.shouldBeOpened();
        Assert.isFalse(jobsTable()::isEmpty);
        MapArray<String, ICell> firstRow = jobsTable().rows(
                "name=QA Specialist",
                "category=Software Test Engineering")
                .first().value;

        Assert.areEquals(firstRow.get("name").getText(), "QA Specialist");
        Assert.areEquals(firstRow.get("category").getText(), "Software Test Engineering");
    }
}
