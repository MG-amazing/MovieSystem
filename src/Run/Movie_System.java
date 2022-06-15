package Run;

import Movie.ben.Business;
import Movie.ben.Customer;
import Movie.ben.Movie;
import Movie.ben.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class Movie_System {
    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    /**
     * 定义系统的数据容器存储用户数据
     * 1.系统中需要存储许多用户对象
     */

    public static final List<User> ALL_USERS = new ArrayList<>();

    /**
     * 存储系统全部商家的排片信息
     * 商家1={p1,p2,p3}
     * 商家2={p2,p3,p4}.....
     * 属于键值对数据
     */
    public static final Map<Business, List<Movie>> ALL_MOVIES = new HashMap<>();
    public static final Scanner SYS_SC = new Scanner(System.in);
    // 定义一个静态的用户对象记住当前用户
    public static User loginUser;

    public static final Logger LOGGER = LoggerFactory.getLogger(Movie_System.class);

    /**
     3.准备几个测试数据
     */
    static {
        Customer c = new Customer();
        c.setLoginName("zyf888");
        c.setPassword("123456");
        c.setUsername("何润东");
        c.setSex('男');
        c.setMoney(10000);
        c.setPhone("110110");
        ALL_USERS.add(c);


        Customer c1 = new Customer();
        c1.setLoginName("hrd666");
        c1.setPassword("123456");
        c1.setUsername("何润西");
        c1.setSex('女');
        c1.setMoney(20000);
        c1.setPhone("12822");
        ALL_USERS.add(c1);


        Business b = new Business();
        b.setUsername("何润南");
        b.setPassword("123456");
        b.setLoginName("hrn123");
        b.setMoney(100);
        b.setSex('男');
        b.setPhone("10086");
        b.setAddress("淄博职业学院南校区");
        b.setShopName("何润南影业");
        ALL_USERS.add(b);
        //商家一定需要加入到店铺排片中去
        List<Movie> movies = new ArrayList<>();
        ALL_MOVIES.put(b, movies);

        Business b2 = new Business();
        b2.setLoginName("hrb123");
        b2.setPassword("123456");
        b2.setUsername("何润北");
        b2.setAddress("淄博职业学院南校区");
        b2.setMoney(1000);
        b2.setSex('女');
        b2.setPhone("10010");
        b2.setShopName("何润北影业");
        ALL_USERS.add(b2);
        //商家一定需要加入到店铺排片中去
        List<Movie> movie3 = new ArrayList<>();
        ALL_MOVIES.put(b2, movie3);
    }

    public static void main(String[] args) {
        showMain();
    }

    /**
     * 首页
     */
    private static void showMain() {

        while (true) {
            System.out.println("----------电影首页--------");
            System.out.println("1.登录");
            System.out.println("2.用户注册");
            System.out.println("3.商家注册");
            System.out.println("请输入操作指令：");
            String command = SYS_SC.nextLine();
            switch (command) {
                case "1":
                    login();
                    //登录界面
                    break;
                case "2":
                    //注册方法
                    break;
                case "3":
                    break;
                default:
                    System.out.println("您输入的命令有误，请确认！");
            }
        }
    }

    /**
     * 登录里面分类用户和商家对象
     */
    private static void login() {
        while (true) {
            System.out.println("请输入登录的名称:");
            String loginName = SYS_SC.nextLine();
            System.out.println("请您输入登录密码:");
            String password = SYS_SC.nextLine();
            //根据登录名查询用户对象
            User u = getUserByLoginName(loginName);
            //2. 判断用户对象是否存在
            if (u != null) {
                //比对密码是否正确
                if (u.getPassword().equals(password)) {
                    loginUser = u;//记住登录成功的用户
                    LOGGER.info(u.getUsername() + "登录系统");
                    //登录成功
                    //判断是用户登录的还是商家登录的
                    if (u instanceof Customer) {
                        //u指向普通客户
                        shoCustomerMain();
                    } else {
                        //u指向商家用户
                        shoBusinessMain();
                    }
                    return;
                } else {
                    System.out.println("密码有问题！！");
                }
            } else {
                System.out.println("用户名称错误，请确认！");
            }
        }
    }

    /**
     * 商家的后台操作界面
     */
    private static void shoBusinessMain() {

        System.out.println("-----------商家界面----------");
        System.out.println(loginUser.getUsername() + (loginUser.getSex() == '男' ? "先生" : "女士") + "欢迎您进入系统");

        while (true) {
            System.out.println("1.展示详情");
            System.out.println("2.上架电影");
            System.out.println("3.下架电影");
            System.out.println("4.修改电影");
            System.out.println("5.退出");
            System.out.println("请输入要操作的指令：");
            String command = SYS_SC.nextLine();
            switch (command) {
                case "1":
                    showBusinessInfos();
                    break;
                case "2":
                    addMovie();
                    break;
                case "3":
                    deleteMovie();
                    break;
                case "4":
                    updateMovie();
                    break;
                case "5":
                    System.out.println(loginUser.getUsername() + "请您记得下次再来~~");
                    return;
                default:
                    System.out.println("您输入的指令我没有找到~~");
            }
        }
    }

    /**
     * 更新电影
     */
    private static void updateMovie() {
        System.out.println("------------修改电影---------");
        Business business = (Business) loginUser;
        List<Movie> movies = ALL_MOVIES.get(business);
        if (movies.size() == 0) {
            System.out.println("当前影片库中没有电影可以修改");
            return;
        }
        while (true) {
            System.out.println("请您输入需要修改的电影名称:");
            String movieName = SYS_SC.nextLine();
            Movie movie = getMovieByName(movieName);

            if (movie != null) {
                System.out.println("请输入修改电影名称");
                String name = SYS_SC.nextLine();
                System.out.println("请输入修改主演");
                String actor = SYS_SC.nextLine();
                System.out.println("请您输入修改时长");
                String time = SYS_SC.nextLine();
                System.out.println("请您输入修改票价");
                String price = SYS_SC.nextLine();
                System.out.println("请您输入修改票数:");
                String totalNumber = SYS_SC.nextLine();

                //去查询有没有这个影片
                while (true) {
                    try {
                        System.out.println("请输入修改后的影片放映时间：");
                        String stime = SYS_SC.nextLine();
                        movie.setName(name);
                        movie.setActor(actor);
                        movie.setPrice(Double.valueOf(price));
                        movie.setTime(Double.valueOf(time));
                        movie.setNumber(Integer.valueOf(totalNumber));
                        movie.setStartTime(sdf.parse(stime));
                        System.out.println("恭喜您成功修改该影片~");
                        showBusinessInfos();
                        return;//干掉方法
                    } catch (Exception e) {
                        e.printStackTrace();
                        LOGGER.error("用户输入错误的时间格式");
                    }
                }
            } else {
                System.out.println("您的店铺没有上架该影片");
                System.out.println("您要继续吗?");
                String command = SYS_SC.nextLine();
                switch (command) {
                    case "Y":
                        break;
                    default:
                        System.out.println("OJBK");
                        return;
                }

            }
        }


    }

    /**
     * 删除电影
     */
    private static void deleteMovie() {
        System.out.println("-----------下架电影------------");
        Business business = (Business) loginUser;
        List<Movie> movies = ALL_MOVIES.get(business);
        if (movies.size() == 0) {
            System.out.println("当前影片库中什么也没有");
            return;
        }
        //让用户输入下架的电影名称
        while (true) {
            System.out.println("请您输入需要下架的电影名称:");
            String movieName = SYS_SC.nextLine();
            //去查询有没有这个影片
            Movie movie = getMovieByName(movieName);
            if (movie != null) {
                movies.remove(movie);
                System.out.println("您当前店铺已成功下架影片!~" + movie.getName());
                showBusinessInfos();
            } else {
                System.out.println("您的店铺没有上架该影片");
                System.out.println("您要继续吗?");
                String command = SYS_SC.nextLine();
                switch (command) {
                    case "Y":
                        break;
                    default:
                        System.out.println("OJBK");
                        return;
                }
            }
        }

    }

    /**
     * 查的是当前账户的电影
     *
     * @param movieName
     * @return
     */
    public static Movie getMovieByName(String movieName) {
        Business business = (Business) loginUser;
        List<Movie> movies = ALL_MOVIES.get(business);
        for (Movie movie : movies) {
            if (movie.getName().contains(movieName)) {
                return movie;
            }
        }
        return null;

    }


    /**
     * 商家进行电影上映
     */
    private static void addMovie() {
        System.out.println("--------------上架电影-------------");
        Business business = (Business) loginUser;
        List<Movie> movies = ALL_MOVIES.get(business);
        System.out.println("请输入新片名：");
        String name = SYS_SC.nextLine();
        System.out.println("请输入主演：");
        String actor = SYS_SC.nextLine();
        System.out.println("请输入时长：");
        String time = SYS_SC.nextLine();
        System.out.println("请输入票价：");
        String price = SYS_SC.nextLine();
        System.out.println("请输入票数：");
        String totalNumber = SYS_SC.nextLine();
        while (true) {
            try {
                System.out.println("请输入影片放映时间：");
                String stime = SYS_SC.nextLine();
                Movie movie = new Movie(name, actor, Double.valueOf(time), Double.valueOf(price), Integer.valueOf(totalNumber), sdf.parse(stime));
                movies.add(movie);
                System.out.println("恭喜您成功上架影片《" + movie.getName() + "》");
                return;//方法挂掉

            } catch (ParseException e) {
                e.printStackTrace();
                LOGGER.error("用户输入错误的时间格式");
            }
        }


    }

    /**
     * 展示商家的详细信息《当前登录的》
     */
    private static void showBusinessInfos() {
        //根据商家对象(loginUser就是登录的用户)，作为Map集合的键提取相应的值作为排片信息Map<Business,List<Movie>>ALL_MOVIES
        Business business = (Business) loginUser;
        System.out.println(business.getShopName() + "\t\t电话：" + business.getPhone() + "\t\t地址：" + business.getAddress());
        List<Movie> movies = ALL_MOVIES.get(business);

        if (movies.size() > 0) {
            System.out.println("片名：\t\t\t\t主演：\t\t时长：\t\t评分：\t\t票价：\t\t余票数量：\t\t放映时间：");
            for (Movie movie : movies) {
                System.out.println(movie.getName() + "\t\t\t" + movie.getActor() + "\t\t\t" + movie.getTime()
                        + "\t\t\t" + movie.getScore() + "\t\t\t" + movie.getPrice() + "\t\t\t" + movie.getNumber() + "\t\t\t" +
                        sdf.format(movie.getStartTime()));
            }
        } else {
            System.out.println("您的店铺没有影片哦~~~");
        }
    }

    /**
     * 客户操作界面
     */
    private static void shoCustomerMain() {


        while (true) {
            System.out.println("-----------客户端界面------------");
            System.out.println(loginUser.getUsername() + (loginUser.getSex() == '男' ? "先生" : "女士") + "欢迎您进入系统");
            System.out.println("请选择您要操作的功能：");
            System.out.println("1.展示全部影片的信息功能");
            System.out.println("2.根据电影名称查询电影信息:");
            System.out.println("3.评分功能");
            System.out.println("4.购票功能");
            System.out.println("5.退出系统");
            System.out.println("请输入您要执行的指令：");
            String command = SYS_SC.nextLine();
            switch (command) {
                case "1":
                    showAllMovies();
                    break;
                case "2":
                    break;
                case "3":
                    break;
                case "4":
                    buyMovie();
                    break;
                case "5":
                    return;
                default:
                    System.out.println("您输入的指令不存在的~~~");
            }
        }
    }

    /**
     * 用户购票功能
     */
    private static void buyMovie() {
        showAllMovies();
        System.out.println("===========购票界面=========");
        System.out.println("请您输入需要买票的门店");
        String shopName = SYS_SC.nextLine();
        //查询是否存在该商家
        Business business = getUserByShopName(shopName);
        if (business == null) {
            System.out.println("对不起,没有该店铺");
        } else {
            //此商家的影片
            List<Movie> movies = ALL_MOVIES.get(business);
            //判断是否存在上架的电影
            if (movies.size() > 0) {
                //开始进行影片的购买
                while (true) {
                    System.out.println("请您输入需要购买的电影名称");
                    String movieName = SYS_SC.nextLine();
                    //去当前商家下去查询该电影对象
                    Movie movie = getMovieByShopAndName(business, movieName);
                    if (movie != null) {
                        //开始购买
                        //判断电影是否购票
                        while (true) {
                            System.out.println("请您输入要购买的电影票数:");
                            String number = SYS_SC.nextLine();
                            int buyNumber = Integer.valueOf(number);
                            if (movie.getNumber() >= buyNumber) {
                                //可以购买了
                                //当前需要花费的金额
                                double money = BigDecimal.valueOf(movie.getPrice()).multiply(BigDecimal.valueOf(buyNumber))
                                        .doubleValue();
                                if (loginUser.getMoney() >= money) {
                                    //可以买票了
                                    System.out.println("您成功购买了"+movie.getName()+",一共"+buyNumber+"张票,金额是"+money);
                                    loginUser.setMoney(loginUser.getMoney()-money);
                                    business.setMoney(business.getMoney()+money);
                                    movie.setNumber(movie.getNumber()-buyNumber);
                                    return;
                                } else {
                                    System.out.println("抱歉,你买不起");
                                    System.out.println("您是否继续买票?y/n");
                                    String command = SYS_SC.nextLine();
                                    switch (command) {
                                        case "y":
                                            break;
                                        case "n":
                                            System.out.println("收到,已处理");
                                            return;
                                        default:
                                            System.out.println("没有该命令默认返回上级方法");
                                            return;
                                    }
                                }
                            } else {
                                //票数不够
                                System.out.println("您当前最多购买" + movie.getNumber());
                                System.out.println("您是否继续买票?y/n");
                                String command = SYS_SC.nextLine();
                                switch (command) {
                                    case "y":
                                        break;
                                    case "n":
                                        System.out.println("收到,已处理");
                                        return;
                                    default:
                                        System.out.println("没有该命令默认返回上级方法");
                                        return;
                                }
                            }
                        }
                    } else {
                        System.out.println("电影名称有误");
                    }
                }
            } else {
                System.out.println("没有该店铺,该店铺已经玩完了~~");
                System.out.println("您是否继续买票?y/n");
                String command = SYS_SC.nextLine();
                switch (command) {
                    case "y":
                        break;
                    case "n":
                        System.out.println("收到,已处理");
                        return;
                    default:
                        System.out.println("没有该命令默认返回上级方法");
                        return;
                }
            }
        }

    }

    public static Movie getMovieByShopAndName(Business business, String name) {
        List<Movie> movies = ALL_MOVIES.get(business);
        for (Movie movie : movies) {
            if (movie.getName().contains(name)) {
                return movie;
            }
        }
        return null;
    }

    /**
     * 根据商家名称返回商家对象
     *
     * @return
     */
    public static Business getUserByShopName(String shopName) {
        Set<Business> businesses = ALL_MOVIES.keySet();
        for (Business business : businesses) {
            if (business.getShopName().equals(shopName)) {
                return business;
            }
        }
        return null;
    }

    /**
     * 展示全部商品
     */
    private static void showAllMovies() {
        ALL_MOVIES.forEach((business, movies) -> {
            System.out.println("==========展示界面===========");

            System.out.println(business.getShopName() + "\t\t电话：" + business.getPhone() + "\t\t地址：" + business.getAddress());
            System.out.println("片名：\t\t\t主演：\t\t\t时长：\t\t评分：\t\t票价：\t\t余票数量：\t\t放映时间：");
            for (Movie movie : movies) {
                System.out.println(movie.getName() + "\t\t\t" + movie.getActor() + "\t\t\t" + movie.getTime()
                        + "\t\t\t" + movie.getScore() + "\t\t\t" + movie.getPrice() + "\t\t\t" + movie.getNumber() + "\t\t\t" +
                        sdf.format(movie.getStartTime()));
            }
        });
    }

    /**
     *
     * @param loginName 用户输入的账户
     * @return
     */
    public static User getUserByLoginName(String loginName) {
        for (User user : ALL_USERS) {
            //判断登录名是不是自己想要的
            if (user.getLoginName().equals(loginName)) {
                return user;
            }
        }
        return null;
        //查无此用户
    }
}
