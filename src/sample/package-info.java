/**
 * Клиент служащий для отображения, редактирования, добавления паттернов. Основанный на технологии REST.
 * <p>
 * <p>Влевой части клиента находятся элементы позволяющие пользователю выбирать нужный ему паттерн.
 * Паттерны разделены на четрыре группы - MV-паттерны, структурные, порождающие, поведенческие.
 * Для отображения подробной информации о паттерне клиент должен кликнуть поего названию в списке.
 * Конпка со значком "+" служит открытия окна с добавление паттеров.
 * <p>Вправой части клиета отображается окно с порбоной инофрмацией о паттерне.
 * Снизу находятся кнопики позволяющие пользователю просмотреть схему паттерна, редактировать его или удалить.
 * Окно редактирования паттерна идентично окну добавления за тем исключением, что при редактировании поля уже содержат информацию о паттерне.
 * Кнопка СhooseSchema позволет выбрать изображение показывающую схему работы паттерна.
 * <P>Вверхней части окна содержится строка поиска и кнопка для обновления списка паттернов.
 * Поиск в строке выполянется по названиям паттернов, при нажатии клавиши Enter выполняется поиск,
 * при нажатии клавиши Esc строка поиска очищается.
 */
package sample;