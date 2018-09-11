package code;

import java.util.Objects;

public class Code {

    private int id;
    private String code;
    private int codeUse;

    public Code() {
    }

    public Code(int id, String code, int codeUse) {
        this.id = id;
        this.code = code;
        this.codeUse = codeUse;
    }

    public String toString() {
        return "#" + id + " " + code + " (" + codeUse + "/3 times used)";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Code code1 = (Code) o;
        return id == code1.id &&
                codeUse == code1.codeUse && Objects.equals(code, code1.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, codeUse);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCodeUse() {
        return codeUse;
    }

    public void setCodeUse(int codeUse) {
        this.codeUse = codeUse;
    }
}
