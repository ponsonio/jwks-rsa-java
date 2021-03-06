package com.auth0.jwk;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class JwkProviderBuilderTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldCreateForDomain() throws Exception {
        assertThat(new JwkProviderBuilder().forDomain("samples.auth0.com").build(), notNullValue());
    }

    @Test
    public void shouldCreateForHttpUrl() throws Exception {
        assertThat(new JwkProviderBuilder().forDomain("https://samples.auth0.com").build(), notNullValue());
    }

    @Test
    public void shouldFailWhenNoUrlIsProvided() throws Exception {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Cannot build provider without domain");
        new JwkProviderBuilder().build();
    }

    @Test
    public void shouldFailWhenOnlySpecifyingCache() throws Exception {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Cannot build provider without domain");
        new JwkProviderBuilder().cached(false).build();
    }

    @Test
    public void shouldCreateCachedProvider() throws Exception {
        JwkProvider provider = new JwkProviderBuilder().rateLimited(false).cached(true).forDomain("samples.auth0.com").build();
        assertThat(provider, notNullValue());
        assertThat(provider, instanceOf(GuavaCachedJwkProvider.class));
        assertThat(((GuavaCachedJwkProvider) provider).getBaseProvider(), instanceOf(UrlJwkProvider.class));
    }

    @Test
    public void shouldCreateCachedProviderWithCustomValues() throws Exception {
        JwkProvider provider = new JwkProviderBuilder().rateLimited(false).cached(10, 24, TimeUnit.HOURS).forDomain("samples.auth0.com").build();
        assertThat(provider, notNullValue());
        assertThat(provider, instanceOf(GuavaCachedJwkProvider.class));
        assertThat(((GuavaCachedJwkProvider) provider).getBaseProvider(), instanceOf(UrlJwkProvider.class));
    }

    @Test
    public void shouldFailWhenOnlySpecifyingRateLimit() throws Exception {
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("Cannot build provider without domain");
        new JwkProviderBuilder().rateLimited(false).build();
    }

    @Test
    public void shouldCreateRateLimitedProvider() throws Exception {
        JwkProvider provider = new JwkProviderBuilder().cached(false).rateLimited(true).forDomain("samples.auth0.com").build();
        assertThat(provider, notNullValue());
        assertThat(provider, instanceOf(RateLimitedJwkProvider.class));
        assertThat(((RateLimitedJwkProvider) provider).getBaseProvider(), instanceOf(UrlJwkProvider.class));
    }

    @Test
    public void shouldCreateRateLimitedProviderWithCustomValues() throws Exception {
        JwkProvider provider = new JwkProviderBuilder().cached(false).rateLimited(10, 24, TimeUnit.HOURS).forDomain("samples.auth0.com").build();
        assertThat(provider, notNullValue());
        assertThat(provider, instanceOf(RateLimitedJwkProvider.class));
        assertThat(((RateLimitedJwkProvider) provider).getBaseProvider(), instanceOf(UrlJwkProvider.class));
    }

    @Test
    public void shouldCreateCachedAndRateLimitedProvider() throws Exception {
        JwkProvider provider = new JwkProviderBuilder().cached(true).rateLimited(true).forDomain("samples.auth0.com").build();
        assertThat(provider, notNullValue());
        assertThat(provider, instanceOf(GuavaCachedJwkProvider.class));
        JwkProvider baseProvider = ((GuavaCachedJwkProvider) provider).getBaseProvider();
        assertThat(baseProvider, instanceOf(RateLimitedJwkProvider.class));
        assertThat(((RateLimitedJwkProvider) baseProvider).getBaseProvider(), instanceOf(UrlJwkProvider.class));
    }

    @Test
    public void shouldCreateCachedAndRateLimitedProviderWithCustomValues() throws Exception {
        JwkProvider provider = new JwkProviderBuilder().cached(10, 24, TimeUnit.HOURS).rateLimited(10, 24, TimeUnit.HOURS).forDomain("samples.auth0.com").build();
        assertThat(provider, notNullValue());
        assertThat(provider, instanceOf(GuavaCachedJwkProvider.class));
        JwkProvider baseProvider = ((GuavaCachedJwkProvider) provider).getBaseProvider();
        assertThat(baseProvider, instanceOf(RateLimitedJwkProvider.class));
        assertThat(((RateLimitedJwkProvider) baseProvider).getBaseProvider(), instanceOf(UrlJwkProvider.class));
    }

    @Test
    public void shouldCreateCachedAndRateLimitedProviderByDefault() throws Exception {
        JwkProvider provider = new JwkProviderBuilder().forDomain("samples.auth0.com").build();
        assertThat(provider, notNullValue());
        assertThat(provider, instanceOf(GuavaCachedJwkProvider.class));
        JwkProvider baseProvider = ((GuavaCachedJwkProvider) provider).getBaseProvider();
        assertThat(baseProvider, instanceOf(RateLimitedJwkProvider.class));
        assertThat(((RateLimitedJwkProvider) baseProvider).getBaseProvider(), instanceOf(UrlJwkProvider.class));
    }
}