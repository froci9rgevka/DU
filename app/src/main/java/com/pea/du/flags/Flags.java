package com.pea.du.flags;

import android.content.Context;

public class Flags {

    //Основные переменные
    public static Integer authorId; //Id автора
    public static String workType; //Дефектация или стадия работы
    public static String workStageType; // Начало/конец/ход работ
    public static Integer addressId; //Id адреса
    public static Integer actId; //Id акта (если выбрана дефектация)
    public static Integer workId; //Id дефекта или работы, если мы редактируем старую. Если создаём новую = -1

    //workType вариации
    final static public String DEFECT = "Дефектация";
    final static public String STAGE_WORK = "Стадия работы";

    final static public String BEGIN_WORK = "Начало";
    final static public String DURING_WORK = "Выполнение";
    final static public String END_WORK = "Завершена";

    //Флаги блокировки активити или кнопок
    public static Boolean isPhotoSending = false;

    // Активити, которое сейчас на экране
    public static Context currentContext;

}
