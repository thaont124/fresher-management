package com.gr.freshermanagement.exception.facility;

import com.gr.freshermanagement.exception.base.NotFoundException;

public class FacilityNotFoundException  extends NotFoundException {
    public FacilityNotFoundException(){
        setMessage("Facility not found");
    }

    public FacilityNotFoundException(String message){
        setMessage(message);
    }
}
