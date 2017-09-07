package com.pea.du.flags;

public class Flags {

    //Основные переменные
    public static Integer authorId; //Id автора
    public static String workType; //Дефектация или Начало/конец/ход работ
    public static Integer addressId; //Id адреса
    public static Integer actId; //Id акта (если выбрана дефектация)
    public static Integer workId; //Id дефекта или работы, если мы редактируем старую. Если создаём новую = -1


    //workType вариации
    final static public String DEFECT = "Defect";
    final static public String BEGIN_WORK = "Begin";
    final static public String DURING_WORK = "During";
    final static public String END_WORK = "End";

}
