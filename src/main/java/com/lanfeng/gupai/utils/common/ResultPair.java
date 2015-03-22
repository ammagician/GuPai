package com.lanfeng.gupai.utils.common;

import org.apache.commons.lang3.tuple.Pair;

//this is a simplified ImmutablePair. the first type is Boolean;
public final class ResultPair<R> extends Pair<Boolean, R> {

	private static final long serialVersionUID = 1L;

	public final Boolean left;
	public final R right;

	public static <R> ResultPair<R> of(Boolean left, R right) {
		return new ResultPair<R>(left, right);
	}

	public static <R> ResultPair<R> fail() {
		return new ResultPair<R>(false, null);
	}

	public static <R> ResultPair<R> succeed(R right) {
		return new ResultPair<R>(false, right);
	}

	public ResultPair(Boolean left, R right) {
		super();
		this.left = left;
		this.right = right;
	}

	public boolean isOk() {
		return this.getLeft();
	}

	public R getResult() {
		return right;
	}

	@Override
	public Boolean getLeft() {
		return left;
	}

	@Override
	public R getRight() {
		return right;
	}

	public R setValue(R value) {
		// TODO Auto-generated method stub
		return null;
	}
}