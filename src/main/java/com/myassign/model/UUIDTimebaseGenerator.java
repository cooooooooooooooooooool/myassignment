package com.myassign.model;

import java.io.Serializable;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;

public class UUIDTimebaseGenerator implements IdentifierGenerator {

	private static TimeBasedGenerator generator = Generators.timeBasedGenerator();

	/*
	 * (non-Javadoc)
	 * @see org.hibernate.id.IdentifierGenerator#generate(org.hibernate.engine.spi.SharedSessionContractImplementor, java.lang.Object)
	 */
	@Override
	public Serializable generate(SharedSessionContractImplementor session, Object object) {
		return generator.generate();
	}
}