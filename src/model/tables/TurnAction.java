package model.tables;

import model.database.annotations.Column;
import model.database.annotations.PrimaryKey;
import model.database.annotations.Table;

@Table("turnaction")
public class TurnAction {

    @PrimaryKey
    @Column("type")
    private String _type;

    public TurnAction(){}

}
