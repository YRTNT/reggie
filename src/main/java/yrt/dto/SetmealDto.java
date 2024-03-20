package yrt.dto;


import lombok.Data;
import yrt.pojo.Setmeal;
import yrt.pojo.SetmealDish;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}

