// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.
package com.azure.cosmos.rx;

import com.azure.cosmos.BridgeInternal;
import com.azure.cosmos.CosmosAsyncClient;
import com.azure.cosmos.CosmosAsyncContainer;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.cosmos.CosmosClientException;
import com.azure.cosmos.CosmosPagedFlux;
import com.azure.cosmos.implementation.CosmosItemProperties;
import com.azure.cosmos.CosmosItemRequestOptions;
import com.azure.cosmos.FeedOptions;
import com.azure.cosmos.FeedResponse;
import com.azure.cosmos.SqlParameter;
import com.azure.cosmos.SqlParameterList;
import com.azure.cosmos.SqlQuerySpec;
import com.azure.cosmos.implementation.Database;
import com.azure.cosmos.implementation.FailureValidator;
import com.azure.cosmos.implementation.FeedResponseListValidator;
import com.azure.cosmos.implementation.FeedResponseValidator;
import com.azure.cosmos.implementation.TestUtils;
import io.reactivex.subscribers.TestSubscriber;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Factory;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class SinglePartitionDocumentQueryTest extends TestSuiteBase {

    private Database createdDatabase;
    private CosmosAsyncContainer createdCollection;
    private List<CosmosItemProperties> createdDocuments = new ArrayList<>();

    private CosmosAsyncClient client;

    public String getCollectionLink() {
        return TestUtils.getCollectionNameLink(createdDatabase.getId(), createdCollection.getId());
    }

    @Factory(dataProvider = "clientBuildersWithDirect")
    public SinglePartitionDocumentQueryTest(CosmosClientBuilder clientBuilder) {
        super(clientBuilder);
    }

    @Test(groups = { "simple" }, timeOut = TIMEOUT, dataProvider = "queryMetricsArgProvider")
    public void queryDocuments(boolean queryMetricsEnabled) throws Exception {

        String query = "SELECT * from c where c.prop = 99";

        FeedOptions options = new FeedOptions();
        options.maxItemCount(5);
        
        options.populateQueryMetrics(queryMetricsEnabled);
        CosmosPagedFlux<CosmosItemProperties> queryObservable = createdCollection.queryItems(query, options, CosmosItemProperties.class);

        List<CosmosItemProperties> expectedDocs = createdDocuments.stream().filter(d -> 99 == d.getInt("prop") ).collect(Collectors.toList());
        assertThat(expectedDocs).isNotEmpty();

        int expectedPageSize = (expectedDocs.size() + options.maxItemCount() - 1) / options.maxItemCount();

        FeedResponseListValidator<CosmosItemProperties> validator = new FeedResponseListValidator.Builder<CosmosItemProperties>()
                .totalSize(expectedDocs.size())
                .exactlyContainsInAnyOrder(expectedDocs.stream().map(d -> d.getResourceId()).collect(Collectors.toList()))
                .numberOfPages(expectedPageSize)
                .pageSatisfy(0, new FeedResponseValidator.Builder<CosmosItemProperties>()
                        .requestChargeGreaterThanOrEqualTo(1.0).build())
                .hasValidQueryMetrics(queryMetricsEnabled)
                .build();

        validateQuerySuccess(queryObservable.byPage(), validator, 10000);
    }

    @Test(groups = { "simple" }, timeOut = TIMEOUT)
    public void queryDocuments_ParameterizedQueryWithInClause() throws Exception {
        String query = "SELECT * from c where c.prop IN (@param1, @param2)";
        SqlParameterList params = new SqlParameterList(new SqlParameter("@param1", 3), new SqlParameter("@param2", 4));
        SqlQuerySpec sqs = new SqlQuerySpec(query, params);

        FeedOptions options = new FeedOptions();
        options.maxItemCount(5);
        
        CosmosPagedFlux<CosmosItemProperties> queryObservable = createdCollection.queryItems(sqs, options, CosmosItemProperties.class);

        List<CosmosItemProperties> expectedDocs = createdDocuments.stream().filter(d -> (3 == d.getInt("prop") || 4 == d.getInt("prop"))).collect(Collectors.toList());
        assertThat(expectedDocs).isNotEmpty();

        int expectedPageSize = (expectedDocs.size() + options.maxItemCount() - 1) / options.maxItemCount();

        FeedResponseListValidator<CosmosItemProperties> validator = new FeedResponseListValidator.Builder<CosmosItemProperties>()
                .totalSize(expectedDocs.size())
                .exactlyContainsInAnyOrder(expectedDocs.stream().map(d -> d.getResourceId()).collect(Collectors.toList()))
                .numberOfPages(expectedPageSize)
                .pageSatisfy(0, new FeedResponseValidator.Builder<CosmosItemProperties>()
                        .requestChargeGreaterThanOrEqualTo(1.0).build())
                .build();

        validateQuerySuccess(queryObservable.byPage(), validator, 10000);
    }

    @Test(groups = { "simple" }, timeOut = TIMEOUT)
    public void queryDocuments_ParameterizedQuery() throws Exception {
        String query = "SELECT * from c where c.prop = @param";
        SqlParameterList params = new SqlParameterList(new SqlParameter("@param", 3));
        SqlQuerySpec sqs = new SqlQuerySpec(query, params);

        FeedOptions options = new FeedOptions();
        options.maxItemCount(5);
        
        CosmosPagedFlux<CosmosItemProperties> queryObservable = createdCollection.queryItems(sqs, options, CosmosItemProperties.class);

        List<CosmosItemProperties> expectedDocs = createdDocuments.stream().filter(d -> 3 == d.getInt("prop")).collect(Collectors.toList());
        assertThat(expectedDocs).isNotEmpty();

        int expectedPageSize = (expectedDocs.size() + options.maxItemCount() - 1) / options.maxItemCount();

        FeedResponseListValidator<CosmosItemProperties> validator = new FeedResponseListValidator.Builder<CosmosItemProperties>()
                .totalSize(expectedDocs.size())
                .exactlyContainsInAnyOrder(expectedDocs.stream().map(d -> d.getResourceId()).collect(Collectors.toList()))
                .numberOfPages(expectedPageSize)
                .pageSatisfy(0, new FeedResponseValidator.Builder<CosmosItemProperties>()
                        .requestChargeGreaterThanOrEqualTo(1.0).build())
                .build();

        validateQuerySuccess(queryObservable.byPage(), validator, 10000);
    }

    @Test(groups = { "simple" }, timeOut = TIMEOUT)
    public void queryDocuments_NoResults() throws Exception {

        String query = "SELECT * from root r where r.id = '2'";
        FeedOptions options = new FeedOptions();
        
        CosmosPagedFlux<CosmosItemProperties> queryObservable = createdCollection.queryItems(query, options, CosmosItemProperties.class);

        FeedResponseListValidator<CosmosItemProperties> validator = new FeedResponseListValidator.Builder<CosmosItemProperties>()
                .containsExactly(new ArrayList<>())
                .numberOfPages(1)
                .pageSatisfy(0, new FeedResponseValidator.Builder<CosmosItemProperties>()
                        .requestChargeGreaterThanOrEqualTo(1.0).build())
                .build();
        validateQuerySuccess(queryObservable.byPage(), validator);
    }

    @Test(groups = { "simple" }, timeOut = TIMEOUT)
    public void queryDocumentsWithPageSize() throws Exception {

        String query = "SELECT * from root";
        FeedOptions options = new FeedOptions();
        options.maxItemCount(3);
        
        CosmosPagedFlux<CosmosItemProperties> queryObservable = createdCollection.queryItems(query, options, CosmosItemProperties.class);

        List<CosmosItemProperties> expectedDocs = createdDocuments;
        int expectedPageSize = (expectedDocs.size() + options.maxItemCount() - 1) / options.maxItemCount();

        FeedResponseListValidator<CosmosItemProperties> validator = new FeedResponseListValidator
            .Builder<CosmosItemProperties>()
            .exactlyContainsInAnyOrder(createdDocuments
                .stream()
                .map(d -> d.getResourceId())
                .collect(Collectors.toList()))
            .numberOfPages(expectedPageSize)
            .allPagesSatisfy(new FeedResponseValidator.Builder<CosmosItemProperties>()
                .requestChargeGreaterThanOrEqualTo(1.0).build())
            .build();

        validateQuerySuccess(queryObservable.byPage(), validator);
    }

    @Test(groups = { "simple" }, timeOut = TIMEOUT)
    public void queryOrderBy() throws Exception {

        String query = "SELECT * FROM r ORDER BY r.prop ASC";
        FeedOptions options = new FeedOptions();
        
        options.maxItemCount(3);
        CosmosPagedFlux<CosmosItemProperties> queryObservable = createdCollection.queryItems(query, options, CosmosItemProperties.class);

        List<CosmosItemProperties> expectedDocs = createdDocuments;
        int expectedPageSize = (expectedDocs.size() + options.maxItemCount() - 1) / options.maxItemCount();

        FeedResponseListValidator<CosmosItemProperties> validator = new FeedResponseListValidator.Builder<CosmosItemProperties>()
                .containsExactly(createdDocuments.stream()
                        .sorted((e1, e2) -> Integer.compare(e1.getInt("prop"), e2.getInt("prop")))
                        .map(d -> d.getResourceId()).collect(Collectors.toList()))
                .numberOfPages(expectedPageSize)
                .allPagesSatisfy(new FeedResponseValidator.Builder<CosmosItemProperties>()
                        .requestChargeGreaterThanOrEqualTo(1.0).build())
                .build();

        validateQuerySuccess(queryObservable.byPage(), validator);
    }

    @Test(groups = { "simple" }, timeOut = TIMEOUT * 1000)
    public void continuationToken() throws Exception {
        String query = "SELECT * FROM r ORDER BY r.prop ASC";
        FeedOptions options = new FeedOptions();
        
        options.maxItemCount(3);
        CosmosPagedFlux<CosmosItemProperties> queryObservable = createdCollection.queryItems(query, options, CosmosItemProperties.class);
        
        TestSubscriber<FeedResponse<CosmosItemProperties>> subscriber = new TestSubscriber<>();
        queryObservable.byPage().take(1).subscribe(subscriber);

        subscriber.awaitTerminalEvent();
        subscriber.assertComplete();
        subscriber.assertNoErrors();
        assertThat(subscriber.valueCount()).isEqualTo(1);
        FeedResponse<CosmosItemProperties> page = ((FeedResponse<CosmosItemProperties>) subscriber.getEvents().get(0).get(0));
        assertThat(page.getResults()).hasSize(3);

        assertThat(page.getContinuationToken()).isNotEmpty();


        options.requestContinuation(page.getContinuationToken());
        queryObservable = createdCollection.queryItems(query, options, CosmosItemProperties.class);

        List<CosmosItemProperties> expectedDocs = createdDocuments.stream().filter(d -> (d.getInt("prop") > 2)).collect(Collectors.toList());
        int expectedPageSize = (expectedDocs.size() + options.maxItemCount() - 1) / options.maxItemCount();

        assertThat(expectedDocs).hasSize(createdDocuments.size() -3);

        FeedResponseListValidator<CosmosItemProperties> validator = new FeedResponseListValidator.Builder<CosmosItemProperties>()
                .containsExactly(expectedDocs.stream()
                        .sorted((e1, e2) -> Integer.compare(e1.getInt("prop"), e2.getInt("prop")))
                        .map(d -> d.getResourceId()).collect(Collectors.toList()))
                .numberOfPages(expectedPageSize)
                .allPagesSatisfy(new FeedResponseValidator.Builder<CosmosItemProperties>()
                        .requestChargeGreaterThanOrEqualTo(1.0).build())
                .build();
        validateQuerySuccess(queryObservable.byPage(), validator);
    }

    @Test(groups = { "simple" }, timeOut = TIMEOUT)
    public void invalidQuerySytax() throws Exception {
        String query = "I am an invalid query";
        FeedOptions options = new FeedOptions();
        
        CosmosPagedFlux<CosmosItemProperties> queryObservable = createdCollection.queryItems(query, options, CosmosItemProperties.class);

        FailureValidator validator = new FailureValidator.Builder()
                .instanceOf(CosmosClientException.class)
                .statusCode(400)
                .notNullActivityId()
                .build();
        validateQueryFailure(queryObservable.byPage(), validator);
    }

    public CosmosItemProperties createDocument(CosmosAsyncContainer cosmosContainer, int cnt) {
        CosmosItemProperties docDefinition = getDocumentDefinition(cnt);
        return BridgeInternal
                   .getProperties(cosmosContainer.createItem(docDefinition, new CosmosItemRequestOptions()).block());
    }

    @BeforeClass(groups = { "simple" }, timeOut = SETUP_TIMEOUT)
    public void before_SinglePartitionDocumentQueryTest() throws Exception {
        client = clientBuilder().buildAsyncClient();
        createdCollection = getSharedSinglePartitionCosmosContainer(client);
        truncateCollection(createdCollection);

        for(int i = 0; i < 5; i++) {
            createdDocuments.add(createDocument(createdCollection, i));
        }

        for(int i = 0; i < 8; i++) {
            createdDocuments.add(createDocument(createdCollection, 99));
        }

        waitIfNeededForReplicasToCatchUp(clientBuilder());
    }

    @AfterClass(groups = { "simple" }, timeOut = SHUTDOWN_TIMEOUT, alwaysRun = true)
    public void afterClass() {
        safeClose(client);
    }

    private static CosmosItemProperties getDocumentDefinition(int cnt) {
        String uuid = UUID.randomUUID().toString();
        CosmosItemProperties doc = new CosmosItemProperties(String.format("{ "
                + "\"id\": \"%s\", "
                + "\"prop\" : %d, "
                + "\"mypk\": \"%s\", "
                + "\"sgmts\": [[6519456, 1471916863], [2498434, 1455671440]]"
                + "}"
                , uuid, cnt, uuid));
        return doc;
    }
}
