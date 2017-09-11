package com.pea.du.db.local.data;

import android.provider.BaseColumns;

public final class Contract {

    private Contract() {
    };

    public static final class GuestEntry implements BaseColumns {
        public final static String USER_TABLE_NAME = "tUser";
        public final static String ACT_TABLE_NAME = "tAct";
        public final static String DEFECT_TABLE_NAME = "tDefect";
        public final static String WORK_TABLE_NAME = "tWork";
        public final static String PHOTO_TABLE_NAME = "tDefectPhoto";
        public final static String ADDRESS_TABLE_NAME = "tAddress";
        public final static String DEFECT_MEASURE_TABLE_NAME = "tMeasure";
        public final static String DEFECT_CONSTRUCTIVE_ELEMENT_TABLE_NAME = "tConstructiveElement";
        public final static String DEFECT_TYPE_TABLE_NAME = "tType";
        public final static String WORK_NAME_TABLE_NAME = "tWorkName";
        public final static String STAGE_TABLE_NAME = "tStage";
        public final static String CONTRACTOR_TABLE_NAME = "tContractor";

        public final static String _ID = BaseColumns._ID;
        public final static String SERVER_ID = "server_id";

        //tUser
        public final static String NICKNAME = "nickname";
        public final static String PASSWORD = "password";

        //tAct
        public final static String USER_ID = "user_id";
        public final static String ADDRESS_ID = "address_id";

        //tDefect
        public final static String ACT_ID = "act_id";
        public final static String DEFECT_CONSTRUCTIVE_ELEMENT_ID = "defect_constructiveElement_id";
        public final static String DEFECT_TYPE_ID = "defect_type_id";
        public final static String PORCH = "porch";
        public final static String FLOOR = "floor";
        public final static String FLAT = "flat";
        public final static String DESCRIPTION = "text";
        public final static String DEFECT_MEASURE_ID = "defect_measure_id";
        public final static String CURRENCY = "cntDefect";

        //tWork
        //public final static String USER_ID = "user_id";
        //public final static String ADDRESS_ID = "address_id";
        public final static String WORK_NAME_ID = "work_name_id";
        public final static String STAGE_ID = "stage_id";
        public final static String CNT = "cnt";
        public final static String MEASURE_ID = "measure_id";
        public final static String DESCR = "descr";
        public final static String SUBCONTRACT = "subcontract";
        public final static String CONTRACTOR_ID = "contractor_id";
        public final static String DOCDATE = "docdate";

        //tPhoto
        public final static String WORK_ID = "work_id";
        public final static String WORKTYPE = "worktype";
        public final static String PATH = "path";
        public final static String URL = "url";

        //tAddress
        public final static String ADDRESS = "address";

        //tMeasure
        public final static String DEFECT_MEASURE = "measure";

        //tConstructiveElement
        public final static String DEFECT_CONSTRUCTIVE_ELEMENT = "ConstructiveElement";

        //tType
        public final static String DEFECT_TYPE = "Type";

        //tWorkName
        public final static String WORK_NAME = "WorkName";

        //tStage
        public final static String STAGE = "Stage";

        //tContractor
        public final static String CONTRACTOR = "Contractor";


    }
}