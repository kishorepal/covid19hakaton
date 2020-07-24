package com.hakaton.covid.bot;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path="test")
public interface GuestbookTestRepository extends 
	PagingAndSortingRepository<GuestbookTest, Long> {
	
}

