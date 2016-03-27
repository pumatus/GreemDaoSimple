### 爱GreenRobot就用GreenDao

之所以写这篇文章,是为了纪念我踩过的坑​:sob:​​:angry:​

**Home page, documentation, and support links: http://greenrobot.org/greendao/**

hithub:https://github.com/greenrobot/greenDAO

那么为什么要用GreenDao呢?

greenDAO is a light & fast ORM solution for Android that maps objects to SQLite databases. Being highly optimized for Android, greenDAO offers great performance and consumes minimal memory.

greenDAO Android是一个轻&快ORM解决方案,将对象映射到SQLite数据库。对Android数据库高度优化,greenDAO提供强大性能和对内存最小的消耗。

我选择相信GreenRoBot这个作者,使用这个轻量级的框架。理由是EventBus同样出自他手，并且从07年就开始从事android开发。

英文好的同学可以去官方网站上阅读了，如果有问题再来看看我这篇文章，说不定可以帮上你。

本人使用的是androidstudio，eclipse的同学还望早点弃坑。

###  帮你生成Dao

没错！！！你不用写Dao，他们会自动生成。

1，在main下创建目录：java-gen。用来存放生成的Dao，必须创建，否则会报错。

![创建位置](http://7xre96.com1.z0.glb.clouddn.com/java-gen%E8%B7%AF%E5%BE%84.png)

2,  新建java工程：new moduel >> Java Library

3，修改bulid.gradle如下 

```groovy
apply plugin: 'java'

dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile 'de.greenrobot:greendao-generator:2.1.0'
}
```

4，新建一个类，类名只要不叫DaoGenerator就行了。。。

```java
public class DaoGen {
    public static void main(String[] args) throws Exception {
        // 正如你所见的，你创建了一个用于添加实体（Entity）的模式（Schema）对象。
        // 两个参数分别代表：数据库版本号与自动生成代码的包路径。
        Schema schema = new Schema(1,"com.example.chenlijin.greendao");
		//      当然，如果你愿意，你也可以分别指定生成的 Bean 与 DAO 类所在的目录，只要如下所示：
		//      Schema schema = new Schema(1, "com.example.chenlijin.bean");
		//      schema.setDefaultJavaPackageDao("com.example.chenlijin.greendao.dao");

        // 模式（Schema）同时也拥有两个默认的 flags，分别用来标示 entity 是否是 activie 以及是否使用 keep sections。
        // schema2.enableActiveEntitiesByDefault();
        // schema2.enableKeepSectionsByDefault();
        // 一旦你拥有了一个 Schema 对象后，你便可以使用它添加实体（Entities）了。
        addNote(schema);
        // 最后我们将使用 DAOGenerator 类的 generateAll() 方法自动生成代码，此处你需要根据自己的情况更改输出目录（既之前创建的 java-gen)。
        // 其实，输出目录的路径可以在 build.gradle 中设置，有兴趣的朋友可以自行搜索，这里就不再详解。
        new DaoGenerator().generateAll(schema, "app/src/main/java-gen");
    }

    /**
     * @param schema
     */
    private static void addNote(Schema schema) {
        // 一个实体（类）就关联到数据库中的一张表，此处表名为「Note」（既类名）
        Entity note = schema.addEntity("Note");
        // 你也可以重新给表命名
        // note.setTableName("NODE");
        // greenDAO 会自动根据实体类的属性值来创建表字段，并赋予默认值
        // 接下来你便可以设置表中的字段：
        note.addIdProperty();
        note.addStringProperty("text").notNull();
        // 与在 Java 中使用驼峰命名法不同，默认数据库中的命名是使用大写和下划线来分割单词的。
        // For example, a property called “creationDate” will become a database column “CREATION_DATE”.
        note.addStringProperty("comment");
        note.addDateProperty("date");
    }
}
```

5，点击右键，运行：

![运行](http://7xre96.com1.z0.glb.clouddn.com/%E8%BF%90%E8%A1%8CDao-gen.png)

到此，Dao自动帮你生成了。

### 开始简化版的数据库体验

在工程bulid.gradle中添加:

```groovy
compile 'de.greenrobot:greendao:2.1.0'
```

把生成的类copy到项目中.

如果不想手动copy:

step 1,修改gradle:

```groovy
android {

    ....

    sourceSets {
        main {
            java.srcDirs = ['src/main/java', 'src/main/java-gen']
        }
    }
}
```

step 2,

 修改 DaoGenerator 中Schema的路径到想要的路径:

Schema schema = new Schema(1,"com.example.chenlijin.greendao");

完成,在运行一遍DaoGenerator,生成的类就会在指定目录里了.

接下来就可以进行数据库的操作了:happy:

创建表

```java
//创建表
DaoMaster.DevOpenHelper db = new DaoMaster.DevOpenHelper(this, "notes-db", null);
```
获取Dao操作类

```java
//获取daomaster
mDaoMaster = new DaoMaster(db.getWritableDatabase());
//创建一个会话
mDaoSession = mDaoMaster.newSession();
//获取NoteDao
mNoteDao = mDaoSession.getNoteDao();
```

添加

```java
private void addNote() {
    String noteText = edittextContent.getText().toString();
    edittextContent.setText("");
    final DateFormat df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM);
    String comment = "添加于:" + df.format(new Date());
    //id可以为空
    Note note = new Note(null, noteText, comment, new Date());
    //Todo 添加
    mNoteDao.insert(note);
    Log.d("DaoExample", "Inserted new note, ID: " + note.getId());

}
```

#### 删除

```java
mNoteDao.delete(noteList.get(position));
```

#### 修改

```java
mNoteDao.delete(noteList.get(position));
```

#### 查询

```java
noteList = mNoteDao.queryBuilder().list();
```

有关查询的文档翻译:http://blog.csdn.net/yuyuanhuang/article/details/42751469
有关查询的官方文档:http://greenrobot.org/greendao/documentation/queries/
基本用法就这些了,更多强大功能,请看官方文档.