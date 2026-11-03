package com.morningwave.cafe.exception;

/**
 * Сутність із заданим id не знайдена.
 * Перевірений (checked) виняток: відсутність запису — очікувана ситуація предметної області
 * (користувач ввів неіснуючий номер), а не помилка програмування, тож компілятор
 * змушує викликача її обробити. Узгоджено з ієрархією MenuException з ЛР3.
 */
public class EntityNotFoundException extends MenuException {

    private final long id;

    public EntityNotFoundException(long id) {
        super("Сутність з id=" + id + " не знайдена");
        this.id = id;
    }

    public long getId() {
        return id;
    }
}
