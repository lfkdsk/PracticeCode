#include<iostream>
#include<string>
#include<map>
#include<vector>
#include<stack>
#include<set>
#include<cstring>
#include<queue>
using namespace std;
map<char,int>getnum;
char findchars[100];          //获得对应字符
vector<string>proce;        //产生式
int table[30][30];         //预测分析表 -1
int tb_s_r[30][30];        //是移进项还是规约项,-1,-2.
int num=0;int numvt=0;   //numvt是终结符集合，0是‘#’，numvt表空字


void readin()                        //读入vt,vn,编号1-num,读入所有产生式
{
    memset(table,-1,sizeof(table));
    getnum['#']=0;
    findchars[0]='#';
    cout<<"请输入终结符集："<<endl;
    char x;
    do
    {
      cin>>x;
      getnum[x]=++num;
      findchars[num]=x;
    }while(cin.peek()!='\n');
     numvt=++num;
     getnum['@']=numvt;        //kong zi
     findchars[num]=('@');
    cout<<"请输入非终结符集："<<endl;
    do
    {
      cin>>x;
      getnum[x]=++num;
      findchars[num]=x;
    }while(cin.peek()!='\n');
    cout<<"输入所有产生式（空字用‘@’表示）,以‘end’结束:"<<endl;
    string pro;
     while(cin>>pro&&pro!="end")
     {
         string ss;
         ss+=pro[0];
         for(int i=3;i<pro.size();i++)
         {
             if(pro[i]=='|')
             {
                  proce.push_back(ss);
                  ss.clear();ss+=pro[0];
             }
             else
             {
                 ss+=pro[i];
             }
         }
          proce.push_back(ss);
    }
}
struct xiangmu         //一个项目
{
    int nump;       //产生式编号
    int id;        //.的位置
    string fst;   //集合
};
string getp[100];   //获得某终结符在左边的产生式集合
void getpp()
{
    for(int i=0;i<proce.size();i++)
    {
        int temp = getnum[proce[i][0]];
        getp[temp] += char('0'+i);
    }
}
string first[100];   //每个符号的first集
bool gotfirst[100]; //是否已经完成FIRST集合
void dfsgetfirst(int nv,int nump)  //当前的符号，和对应产生式编号
{
   int temp=getnum[proce[nump][1]];  //产生式推出来的首符
    gotfirst[nump]=1;               //标记
    if(temp<=numvt)first[nv]+=char('0'+temp);  //是终结符
    else
    {
        for(int i=0;i<getp[temp].size();i++)    //所有temp可以推出来的符号对应的产生式
          {
              if(proce[nump][0]==proce[nump][1])continue; //左递归的产生式不用不影响求fisrt集
              dfsgetfirst(temp,getp[temp][i]-'0');
          }

        first[nv]+=first[temp];                  //回溯时候沿途保存
    }
}
void get_first()
{
    for(int i=1;i<=numvt;i++)             //    终结符first集合是它自己.
    {
        first[i]=char('0'+i);
    }
     for(int i=0;i<proce.size();i++)
    {
        if(proce[i][0]==proce[i][1])continue; //左递归的产生式不用不影响求fisrt集
        if(gotfirst[i])continue;              //已经生成。
        int temp=getnum[proce[i][0]];
          dfsgetfirst(temp,i);
    }
}
vector<vector<xiangmu> >v;             //项目集族
int e[100][3]; int head[100];int nume=0;    //链式前向星项目集族图
void addegde(int from,int to,int w)         //添加边
{
    e[nume][0]=to;
    e[nume][1]=head[from];head[from]=nume;
    e[nume++][2]=w;
}
void clear()                 //初始化函数
{
    for(int i=0;i<100;i++)
       head[i]=-1;
     for(int i=0;i<30;i++)
       for(int j=0;j<30;j++)
         tb_s_r[i][j]=table[i][j]=-1;
    nume=0;
}
inline bool xmeq(xiangmu a,xiangmu b)
{
    if(a.fst==b.fst&&a.id==b.id&&a.nump==b.nump)return 1;
    return 0;
}
bool isin(xiangmu a,vector<xiangmu> b)      //xm a is in xmji b
{
    for(int i=0;i<b.size();i++)
    {
        if(xmeq(a,b[i]))return 1;
    }
    return 0;
}
vector<xiangmu>  hebing(vector<xiangmu>a ,vector<xiangmu>b)  //合并项目集 a,b 复给 a
{
    for(int i=0;i<b.size();i++)
    {
        if(isin(b[i],a))continue;
        else
         a.push_back(b[i]);
    }
    return a;
}
bool xmjieq(vector<xiangmu> a,vector<xiangmu> b)  //两个项目集是否相等
{
    if(a.size()!=b.size())return 0;
     for(int i=0;i<a.size();i++)
     {
        if(!isin(a[i],b))return 0;
     }
     return 1;
}
int xmji_isin_xmjizu(vector<xiangmu>a,vector<vector<xiangmu> >b)  //查找项目集，若有，则返回编号,一举俩得
{
    for(int i=0;i<b.size();i++)
    {
        if(xmjieq(a,b[i]))return i;
    }
    return -1;
}
vector<xiangmu> get_close(xiangmu t)           //对项目 T作闭包
{
   vector<xiangmu> temp;
   temp.push_back(t);
    queue<xiangmu> q;                         //bfs完成闭包
    q.push(t);
    while(!q.empty())
    {
      xiangmu cur = q.front();
      q.pop();
      if(cur.id == proce[cur.nump].size())          //归约项舍去
          continue;
     int tt=getnum[proce[cur.nump][cur.id]];       //tt is thm num of '.'zhihoudefuhao
      if(tt <= numvt)   continue;                  //若是终结符，则不必找了
        cout<< "getp size" << getp[tt].size()<<endl;
        cout<< proce[cur.nump]<<" fuck"<<endl;
      for(int i=0;i<getp[tt].size();i++)         //对应产生式的编号
       {
          xiangmu c;
         c.id=1;                               //
         c.nump=getp[tt][i]-'0';             //
           
        if(proce[cur.nump].size()-cur.id==1)   // the last : A->BC.D,a/b
          c.fst+=cur.fst;// 参见我的笔记
         else                           // not the last  ：A->B.CFb,a/b
        {
          int tttnum = getnum[proce[cur.nump][cur.id+1]];
            
          c.fst += first[tttnum];
        }
         if(!isin(c,temp))           // 排重，新的项目就加入。
         {
             q.push(c);
             temp.push_back(c);
         }
        }
      }
      return temp;
}

void print_close(vector<xiangmu> item){
    for (int i =0; i < item.size(); i++) {
        cout << i << " : " << endl;
        cout << item[i].id << " : " << item[i].fst << endl;
    }
}
void get_xiangmujizu()             //获得项目集族
{
    vector<xiangmu>temp;
    xiangmu t;
    t.nump=0;t.id=1;t.fst+='0';    //初始的项目集：0
    temp=get_close(t);
    print_close(temp);
    queue<vector<xiangmu> >q;        //bfs法获得
    q.push(temp);
    v.push_back(temp);             //第一个入
    while(!q.empty())
    {
         vector<xiangmu> cur = q.front();
         q.pop();
         for(int i=1;i<=num;i++)     //所有符号
         {
             if(i==numvt)continue;      //'#'
             vector<xiangmu> temp;
              for(int j=0;j<cur.size();j++)     //该项目集中的所有项目
              {
                 if(cur[j].id==proce[cur[j].nump].size())continue;  //是规约项目，无法再读入了
                 int tt=getnum[proce[cur[j].nump][cur[j].id]];
                if(tt==i)                                          //can read in 符号i
                {
                    xiangmu tempt;
                    tempt.fst=cur[j].fst;
                    tempt.id=cur[j].id+1;
                    tempt.nump=cur[j].nump;
                    temp=hebing(temp,get_close(tempt));
                }
              }
              if(temp.size()==0)continue;             //该符号无法读入。
                int numcur=xmji_isin_xmjizu(cur,v);   //当前节点标号
                int tttnum=xmji_isin_xmjizu(temp,v);  //新目标标号
                   if(tttnum==-1)                    //新的项目集
                   {
                    v.push_back(temp);
                    q.push(temp);
                    addegde(numcur,v.size()-1,i) ;   //添加边，权为读入的符号
                  }
                   else                             //老的项目集
                   {
                    addegde(numcur,tttnum,i);
                   }
         }
    }
}



void print_xmjizu()              //打印项目集族
{
    for(int i=0;i<v.size();i++)
    {
        cout<<"项目集"<<i<<":"<<endl;
      for(int j=0;j<v[i].size();j++)
        {
          cout<<proce[v[i][j].nump]<<" "<<v[i][j].id<<" "<<v[i][j].fst<<endl;
        }
      cout<<endl;
    }
    for(int i=0;i<v.size();i++)
    {
        for(int j=head[i];j!=-1;j=e[j][1])
        {
            cout<<"  "<<findchars[e[j][2]]<<endl;
            cout<<i<<"--->"<<e[j][0]<<endl;
        }
    }
}
bool get_table()            //获得分析表table[i][j]=w:状态i-->j,读入符号W。
{
    for(int i=0;i<v.size();i++)          //遍历图
    {
        for(int j=head[i];j!=-1;j=e[j][1])
        {
            if(table[i][e[j][2]]!=-1)return 0;           //多重入口，报错.
             table[i][e[j][2]]=e[j][0];
            tb_s_r[i][e[j][2]]=-1;             //移近项-1。
        }
    }
    for(int i=0;i<v.size();i++)             //遍历所有项目
    {
      for(int j=0;j<v[i].size();j++)
        {
            if(v[i][j].id==proce[v[i][j].nump].size())  //归约项
            {
                for(int k=0;k<v[i][j].fst.size();k++)
                   {
                      if(table[i][(v[i][j].fst)[k]-'0']!=-1)return 0;           //多重入口，报错.
                     if(  (v[i][j].fst)[k]=='0'&&v[i][j].nump==0)
                        table[i][(v[i][j].fst)[k]-'0']=-3 ;           //接受态。
                     else
                     {
                        table[i][(v[i][j].fst)[k]-'0']=v[i][j].nump;
                        tb_s_r[i][(v[i][j].fst)[k]-'0']=-2;            //归约态
                     }
                   }
            }
         }
     }
     return 1;
}
void print_table()
{
    cout<<"LR(1)分析表："<<endl;
    cout<<"状态   "<<"         actoin     "<<endl;
     for(int j=0;j<=num;j++)
        {
            if(j==numvt)continue;
            cout<<"    "<<findchars[j];
        }
       cout<<endl;
    for(int i=0;i<v.size();i++)
    {
        cout<<i<<"   ";
        for(int j=0;j<=num;j++)
        {
            if(j==numvt)continue;
            if(table[i][j]==-3)  cout<<"acc"<<"  ";       //接受
            else if(table[i][j]==-1)cout<<"     ";        //空
            else if(tb_s_r[i][j]==-1)cout<<"s"<<table[i][j]<<"   ";  //移近
            else if(tb_s_r[i][j]==-2)cout<<"r"<<table[i][j]<<"   ";  //归约
        }
        cout<<endl;
    }
}
string word;
void  print_now_state(int count,stack<int>state,stack<int>wd,int i)
{
    cout<<count<<'\t'<<'\t';
    stack<int>temp;
    while(!state.empty())
    {
        temp.push(state.top());
        state.pop();
    }
    while(!temp.empty())
    {
        cout<<temp.top();
        temp.pop();
    }
    cout<<'\t'<<'\t';
     while(!wd.empty())
    {
        temp.push(wd.top());
        wd.pop();
    }
    while(!temp.empty())
    {
        cout<<findchars[temp.top()];
        temp.pop();
    }
    cout<<'\t'<<'\t';
    for(int j=i;j<word.size();j++)
        cout<<word[j];
    cout<<'\t'<<'\t';
}

bool analyze()
{
    cout<<"       "<<word<<"的分析过程："<<endl;
    cout<<"步骤\t\t"<<"状态栈\t\t"<<"符号栈\t\t"<<"输入串\t\t"<<"动作说明"<<endl;
      stack<int>state;   //俩个栈：状态栈和符号栈
      stack<int>wd;
      int count=0;
      state.push(0);     //初始化
      wd.push(0);        //'#'
    for(int i=0;;)       //i，读入文本的
    {
        int cur=state.top();
        if(table[cur][getnum[word[i]]]==-1)    // 空白，报错误
             return 0;
        if(table[cur][getnum[word[i]]]==-3)  //接受态
            {
                print_now_state(count++,state,wd,i);
                cout<<"      恭喜！acc!"<<endl;
                return 1;
            }
        if(tb_s_r[cur][getnum[word[i]]]==-1)       //移进项
        {
            print_now_state(count++,state,wd,i);
           int newstate=table[cur][getnum[word[i]]];
            cout<<"action["<<cur<<","<<getnum[word[i]]<<"]="<<newstate;
            cout<<"，状态"<<newstate<<"入栈"<<endl;
            wd.push(getnum[word[i]]);
            state.push(newstate);
            i++;
        }
        else if(tb_s_r[cur][getnum[word[i]]]==-2)         //归约
        {
            print_now_state(count++,state,wd,i);

             int numpro=table[cur][getnum[word[i]]];   //用该产生式归约
            int len=proce[numpro].size()-1;
            for(int ii=0;ii<len;ii++)                 //弹栈
             {
                 wd.pop();
                 state.pop();
             }
             wd.push(getnum[proce[numpro][0]]);    //新入
             int cur=state.top();
            cout<<"用"<<proce[numpro][0]<<"->";
             for(int ii=1;ii<=len;ii++)
                 cout<<proce[numpro][ii];
            cout<<"进行归约,"<<"goto["<<cur<<","<<getnum[word[i]]<<"]="<<table[cur][getnum[proce[numpro][0]]];
            cout<<"入栈"<<endl;
             state.push(table[cur][getnum[proce[numpro][0]]]);
        }
    }
    return 1;
}
int main()
{
    clear();
    readin();
    getpp();
    get_first();
    get_xiangmujizu();
    if(!get_table())
    {
        cout<<"此文法在生成分析表时候有多重入口，非LR(1)文法！"<<endl;
        return 0;
    }
   print_xmjizu();
   print_table();
   cout<<"请输入字："<<endl;
   cin>>word;
   word+='#';
   if(!analyze())
       cout<<"error!"<<endl;
   else;
    return 0;
}
