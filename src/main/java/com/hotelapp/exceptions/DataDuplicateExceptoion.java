package com.hotelapp.exceptions;

public class DataDuplicateExceptoion extends RuntimeException {

    public DataDuplicateExceptoion(String datafield,String dataValue){
        super(datafield+" "+dataValue + " exists in database");
    }
}
