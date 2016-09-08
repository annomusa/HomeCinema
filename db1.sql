/*==============================================================*/
/* DBMS name:      Sybase SQL Anywhere 10                       */
/* Created on:     9/7/2016 6:13:48 PM                          */
/*==============================================================*/


/*==============================================================*/
/* Table: CHANNEL                                               */
/*==============================================================*/
create table CHANNEL 
(
   ID_CHANNEL           integer                        not null,
   NAME_CHANNEL         varchar(10)                    null,
   DESCRIPTION          varchar(50)                    null,
   constraint PK_CHANNEL primary key (ID_CHANNEL)
);

/*==============================================================*/
/* Index: CHANNEL_PK                                            */
/*==============================================================*/
create unique index CHANNEL_PK on CHANNEL (
ID_CHANNEL ASC
);

/*==============================================================*/
/* Table: FILM                                                  */
/*==============================================================*/
create table FILM 
(
   ID_FILM              integer                        not null,
   NAME_FILM            varchar(50)                    null,
   PLOT                 varchar(150)                   null,
   constraint PK_FILM primary key (ID_FILM)
);

/*==============================================================*/
/* Index: FILM_PK                                               */
/*==============================================================*/
create unique index FILM_PK on FILM (
ID_FILM ASC
);

/*==============================================================*/
/* Table: JADWAL                                                */
/*==============================================================*/
create table JADWAL 
(
   ID_JADWAL            integer                        not null,
   ID_FILM              integer                        null,
   ID_CHANNEL           integer                        null,
   WAKTU                time                           null,
   TANGGAL              date                           null,
   constraint PK_JADWAL primary key (ID_JADWAL)
);

/*==============================================================*/
/* Index: JADWAL_PK                                             */
/*==============================================================*/
create unique index JADWAL_PK on JADWAL (
ID_JADWAL ASC
);

/*==============================================================*/
/* Index: RELATIONSHIP_1_FK                                     */
/*==============================================================*/
create index RELATIONSHIP_1_FK on JADWAL (
ID_CHANNEL ASC
);

/*==============================================================*/
/* Index: RELATIONSHIP_2_FK                                     */
/*==============================================================*/
create index RELATIONSHIP_2_FK on JADWAL (
ID_FILM ASC
);

alter table JADWAL
   add constraint FK_JADWAL_RELATIONS_CHANNEL foreign key (ID_CHANNEL)
      references CHANNEL (ID_CHANNEL)
      on update restrict
      on delete restrict;

alter table JADWAL
   add constraint FK_JADWAL_RELATIONS_FILM foreign key (ID_FILM)
      references FILM (ID_FILM)
      on update restrict
      on delete restrict;

