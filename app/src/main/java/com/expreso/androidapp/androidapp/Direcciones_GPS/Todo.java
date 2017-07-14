package com.expreso.androidapp.androidapp.Direcciones_GPS;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Todo {

@SerializedName("id")
@Expose
private Integer id;
@SerializedName("RUC")
@Expose
private String rUC;
@SerializedName("corporativo")
@Expose
private Integer corporativo;
@SerializedName("razon_social")
@Expose
private String razonSocial;
@SerializedName("entidad")
@Expose
private Integer entidad;
@SerializedName("address")
@Expose
private String address;
@SerializedName("contacto")
@Expose
private String contacto;
@SerializedName("telefono")
@Expose
private String telefono;
@SerializedName("geolocation")
@Expose
private String geolocation;

public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public String getRUC() {
return rUC;
}

public void setRUC(String rUC) {
this.rUC = rUC;
}

public Integer getCorporativo() {
return corporativo;
}

public void setCorporativo(Integer corporativo) {
this.corporativo = corporativo;
}

public String getRazonSocial() {
return razonSocial;
}

public void setRazonSocial(String razonSocial) {
this.razonSocial = razonSocial;
}

public Integer getEntidad() {
return entidad;
}

public void setEntidad(Integer entidad) {
this.entidad = entidad;
}

public String getAddress() {
return address;
}

public void setAddress(String address) {
this.address = address;
}

public String getContacto() {
return contacto;
}

public void setContacto(String contacto) {
this.contacto = contacto;
}

public String getTelefono() {
return telefono;
}

public void setTelefono(String telefono) {
this.telefono = telefono;
}

public String getGeolocation() {
return geolocation;
}

public void setGeolocation(String geolocation) {
this.geolocation = geolocation;
}

}