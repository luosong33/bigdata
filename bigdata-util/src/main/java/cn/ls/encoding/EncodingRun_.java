package cn.ls.encoding;

import java.io.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Administrator on 2017/3/3.
 */
public class EncodingRun_ {

    public static void main(String[] args) throws UnsupportedEncodingException {
        new EncodingRun_("E:\\picc\\shell\\ceshi\\data\\car_prpcinsured_1100_2017-03-06-141000.unl");
    }

    public EncodingRun_(String filePath) throws UnsupportedEncodingException {
        try {
            FileOutputStream out=new FileOutputStream(new File(filePath+"_back"), true);
            ConcurrentLinkedQueue<String> queue=new ConcurrentLinkedQueue<String>();
            for(int i=0;i<10;i++){
                new Thread(new MyThread(queue,filePath)).start();//多线程往队列中写入数据
            }
            new Thread(new DealFile(queue,out)).start();//监听线程，不断从queue中读数据写入到文件中
            try {
                Thread.sleep(3000);
                if(!Thread.currentThread().isAlive()){
                    System.out.println("线程已结束");
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将要写入文件的数据存入队列中
     *
     * @author Administrator
     */
    class MyThread implements Runnable {
        private ConcurrentLinkedQueue<String> queue;
        private String contents;
        private String filepath_ = "";

        public MyThread(ConcurrentLinkedQueue<String> queue, String filepath_) {
            this.queue = queue;
            this.filepath_ = filepath_;
        }

        public void run() {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            long begin = System.currentTimeMillis();
            BufferedReader reader = null;
            BufferedWriter writer = null;
            try {
                System.out.println();
                reader = new BufferedReader(new InputStreamReader(
                        new FileInputStream(filepath_), "gbk"));
                String stry = "";

                synchronized (reader) {
                    while ((contents = reader.readLine()) != null) { // 当读取的一行不为空时,把读到的str的值赋给str1
                        contents += "\r\n";
                    }
                }
                long end = System.currentTimeMillis();
                System.out.println("encoding:" + (end - begin) + "豪秒");
            } catch (FileNotFoundException e) {
                System.out.println("找不到指定文件");
            } catch (IOException e) {
                System.out.println("读取文件失败");
            } finally {
                try {
                    writer.close(); // 先开后关
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            queue.add(contents);
        }
    }

    /**
     * 将队列中的数据写入到文件
     *
     * @author Administrator
     */
    class DealFile implements Runnable {

        private ConcurrentLinkedQueue<String> queue;
        private String filepath_;
        private BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(new File(filepath_ + "_back")), "gbk"));
        private FileOutputStream out;

        public DealFile(ConcurrentLinkedQueue<String> queue,FileOutputStream out) throws UnsupportedEncodingException, FileNotFoundException {
            this.queue = queue;
//            this.filepath_ = filepath_;
            this.out = out;
        }

        public void run() {
            synchronized (queue) {
                while (true) {
                    if (!queue.isEmpty()) {
                        try {
                            out.write(queue.poll().getBytes("UTF-8"));
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


}
