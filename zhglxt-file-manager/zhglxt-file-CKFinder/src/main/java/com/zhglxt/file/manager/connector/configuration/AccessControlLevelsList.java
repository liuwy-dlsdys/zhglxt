/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zhglxt.file.manager.connector.configuration;

import com.zhglxt.file.manager.connector.utils.AccessControlUtil;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Custom {@link java.util.ArrayList} used to manage ACLs in application. The list also manages the state of ACL entries in
 * {@code AccesControlUtil} class by removing them from that class if list is marked as modified.<br>
 * Please note that when adding/removing items from existing {@code AccessControlLevelsList} or creating new {@code AccessControlLevelsList}
 * in {@link Configuration#prepareConfigurationForRequest(javax.servlet.http.HttpServletRequest)}
 * method, the modified flag should always be set to {@code true}.
 *
 * @param <E> type of elelmens which will be kept in {@code AccessControlLevelsList}
 */
public class AccessControlLevelsList<E> extends ArrayList<E> {

    /**
     * Constructs an empty list with an initial capacity of ten.
     *
     * @param modified marker indicating whether ACL entries in {@code AccesControlUtil} should be reset. This parameter should be set to
     *                 true whenever new list is created in
     *                 {@link Configuration#prepareConfigurationForRequest(javax.servlet.http.HttpServletRequest)} or
     *                 elements are added/removed from existing {@code AccessControlLevelsList}.
     */
    public AccessControlLevelsList(boolean modified) {
        super();
        if (modified) {
            resetACLUtilConfiguration();
        }
    }

    /**
     * Constructs an empty list with the specified initial capacity.
     *
     * @param capacity the initial capacity of the list
     * @param modified marker indicating whether ACL entries in {@code AccesControlUtil} should be reset. This parameter should be set to
     *                 true whenever new list is created in
     *                 {@link Configuration#prepareConfigurationForRequest(javax.servlet.http.HttpServletRequest)} or
     *                 elements are added/removed from existing {@code AccessControlLevelsList}.
     * @throws IllegalArgumentException if the specified initial capacity is negative
     */
    public AccessControlLevelsList(int capacity, boolean modified) {
        super(capacity);
        if (modified) {
            resetACLUtilConfiguration();
        }
    }

    /**
     * Constructs a list containing the elements of the specified collection, in the order they are returned by the collection's iterator.
     *
     * @param c        the collection which elements are to be placed in this list
     * @param modified marker indicating whether ACL entries in {@code AccesControlUtil} should be reset. This parameter should be set to
     *                 true whenever new list is created in
     *                 {@link Configuration#prepareConfigurationForRequest(javax.servlet.http.HttpServletRequest)} or
     *                 elements are added/removed from existing {@code AccessControlLevelsList}.
     * @throws NullPointerException if the specified collection is null
     */
    public AccessControlLevelsList(Collection<? extends E> c, boolean modified) {
        super(c);
        if (modified) {
            resetACLUtilConfiguration();
        }
    }

    /**
     * Appends the specified element to the end of this list and resets ACL entries in {@code AccesControlUtil} if necessary.
     *
     * @param item     element to be appended to this list
     * @param modified marker indicating whether ACL entries in {@code AccesControlUtil} should be reset. This parameter should be set to
     *                 true whenever new list is created in
     *                 {@link Configuration#prepareConfigurationForRequest(javax.servlet.http.HttpServletRequest)} or
     *                 elements are added/removed from existing {@code AccessControlLevelsList}.
     * @return <tt>true</tt> (as specified by {@link Collection#add})
     */
    public boolean addItem(E item, boolean modified) {
        if (modified) {
            resetACLUtilConfiguration();
        }
        return super.add(item);
    }

    /**
     * Inserts the specified element at the specified position in this list. Shifts the element currently at that position (if any) and any
     * subsequent elements to the right (adds one to their indices). This method also resets ACL entries in {@code AccesControlUtil} if
     * necessary.
     *
     * @param index    index at which the specified element is to be inserted
     * @param item     element to be inserted
     * @param modified marker indicating whether ACL entries in {@code AccesControlUtil} should be reset. This parameter should be set to
     *                 true whenever new list is created in
     *                 {@link Configuration#prepareConfigurationForRequest(javax.servlet.http.HttpServletRequest)} or
     *                 elements are added/removed from existing {@code AccessControlLevelsList}.
     * @throws IndexOutOfBoundsException when the index is out of range (index lower than 0 or greater than list size).
     */
    public void addItem(int index, E item, boolean modified) {
        if (modified) {
            resetACLUtilConfiguration();
        }
        super.add(index, item);
    }

    /**
     * Appends all of the elements in the specified collection to the end of this list, in the order that they are returned by the specified
     * collection's Iterator. The behavior of this operation is undefined if the specified collection is modified while the operation is in
     * progress. (This implies that the behavior of this call is undefined if the specified collection is this list, and this list is
     * nonempty.)<br>
     * This method also resets ACL entries in {@code AccesControlUtil} if necessary.
     *
     * @param c        collection containing elements to be added to this list
     * @param modified marker indicating whether ACL entries in {@code AccesControlUtil} should be reset. This parameter should be set to
     *                 true whenever new list is created in
     *                 {@link Configuration#prepareConfigurationForRequest(javax.servlet.http.HttpServletRequest)} or
     *                 elements are added/removed from existing {@code AccessControlLevelsList}.
     * @return <tt>true</tt> if this list changed as a result of the call
     * @throws NullPointerException if the specified collection is null
     */
    public boolean addAllItems(Collection<? extends E> c, boolean modified) {
        if (modified) {
            resetACLUtilConfiguration();
        }
        return super.addAll(c);
    }

    /**
     * Inserts all of the elements in the specified collection into this list, starting at the specified position. Shifts the element
     * currently at that position (if any) and any subsequent elements to the right (increases their indices). The new elements will appear
     * in the list in the order that they are returned by the specified collection's iterator.<br>
     * This method also resets ACL entries in {@code AccesControlUtil} if necessary.
     *
     * @param index    index at which to insert the first element from the specified collection
     * @param c        collection containing elements to be added to this list
     * @param modified marker indicating whether ACL entries in {@code AccesControlUtil} should be reset. This parameter should be set to
     *                 true whenever new list is created in
     *                 {@link Configuration#prepareConfigurationForRequest(javax.servlet.http.HttpServletRequest)} or
     *                 elements are added/removed from existing {@code AccessControlLevelsList}.
     * @return <tt>true</tt> if this list changed as a result of the call
     * @throws IndexOutOfBoundsException when the index is out of range (index lower than 0 or greater than list size).
     * @throws NullPointerException      if the specified collection is null.
     */
    public boolean addAllItems(int index, Collection<? extends E> c, boolean modified) {
        if (modified) {
            resetACLUtilConfiguration();
        }
        return super.addAll(index, c);
    }

    /**
     * Removes the element at the specified position in this list. Shifts any subsequent elements to the left (subtracts one from their
     * indices). This method also resets ACL entries in {@code AccesControlUtil} if necessary.
     *
     * @param index    the index of the element to be removed
     * @param modified marker indicating whether ACL entries in {@code AccesControlUtil} should be reset. This parameter should be set to
     *                 true whenever new list is created in
     *                 {@link Configuration#prepareConfigurationForRequest(javax.servlet.http.HttpServletRequest)} or
     *                 elements are added/removed from existing {@code AccessControlLevelsList}.
     * @return the element that was removed from the list
     * @throws IndexOutOfBoundsException when the index is out of range (index lower than 0 or greater than list size).
     */
    public E removeItem(int index, boolean modified) {
        if (modified) {
            resetACLUtilConfiguration();
        }
        return super.remove(index);
    }

    /**
     * Removes the first occurrence of the specified element from this list, if it is present. If the list does not contain the element, it
     * is unchanged. More formally, removes the element with the lowest index
     * <tt>i</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>
     * (if such an element exists). Returns <tt>true</tt> if this list contained the specified element (or equivalently, if this list
     * changed as a result of the call).<br>
     * This method also resets ACL entries in {@code AccesControlUtil} if necessary.
     *
     * @param item     element to be removed from this list, if present
     * @param modified marker indicating whether ACL entries in {@code AccesControlUtil} should be reset. This parameter should be set to
     *                 true whenever new list is created in
     *                 {@link Configuration#prepareConfigurationForRequest(javax.servlet.http.HttpServletRequest)} or
     *                 elements are added/removed from existing {@code AccessControlLevelsList}.
     * @return <tt>true</tt> if this list contained the specified element
     */
    public boolean removeItem(E item, boolean modified) {
        if (modified) {
            resetACLUtilConfiguration();
        }
        return super.remove(item);
    }

    /**
     * Removes from this list all of its elements that are contained in the specified collection. This method also resets ACL entries in
     * {@code AccesControlUtil} if necessary.
     *
     * @param c        collection containing elements to be removed from this list
     * @param modified marker indicating whether ACL entries in {@code AccesControlUtil} should be reset. This parameter should be set to
     *                 true whenever new list is created in
     *                 {@link Configuration#prepareConfigurationForRequest(javax.servlet.http.HttpServletRequest)} or
     *                 elements are added/removed from existing {@code AccessControlLevelsList}.
     * @return {@code true} if this list changed as a result of the call
     * @throws ClassCastException   if the class of an element of this list is incompatible with the specified collection
     *                              (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if this list contains a null element and the specified collection does not permit null elements
     *                              (<a href="Collection.html#optional-restrictions">optional</a>), or if the specified collection is null
     * @see Collection#contains(Object)
     */
    public boolean removeAllItems(Collection<?> c, boolean modified) {
        if (modified) {
            resetACLUtilConfiguration();
        }
        return super.removeAll(c);
    }

    /**
     * Removes all of the elements from this list. The list will be empty after this call returns.<br>
     * This method also resets ACL entries in {@code AccesControlUtil} if necessary.
     *
     * @param modified marker indicating whether ACL entries in {@code AccesControlUtil} should be reset. This parameter should be set to
     *                 true whenever new list is created in
     *                 {@link Configuration#prepareConfigurationForRequest(javax.servlet.http.HttpServletRequest)} or
     *                 elements are added/removed from existing {@code AccessControlLevelsList}.
     */
    public void clear(boolean modified) {
        if (modified) {
            resetACLUtilConfiguration();
        }
        super.clear();
    }

    /**
     * Resets configuration and ACL entries in {@code AccesControlUtil} instance.
     */
    private void resetACLUtilConfiguration() {
        AccessControlUtil.getInstance().resetConfiguration();
    }
}
