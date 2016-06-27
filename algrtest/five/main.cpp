#include<iostream>
#include<queue>
using namespace std;

int mArray[100];
int page[100];
int pageSize=3,size;
bool bre;

bool isEqual(queue<int> que,int num)
{
    while(!que.empty())
    {
        if(que.front()==num)
            return true;
        que.pop();
    }
    return false;
}
void print(queue<int> que,int p)
{
    cout<<"调用的页面为:"<<p;
    if(bre)
        cout<<"    产生中断: ";
    else
        cout<<"    不产生中断: ";
    while(!que.empty())
    {
        cout<<que.front()<<" ";
        que.pop();
    }
    cout<<endl;
}
queue<int> LRU_update(queue<int> que,int num)
{
    queue<int> que1;
    while(!que.empty())
    {
        if(que.front()!=num)
        {
            que1.push(que.front());
        }
        que.pop();
    }
    que1.push(num);
    return que1;
}
queue<int> OPT_update(queue<int> que,int insert_element,int remove_element)
{
    queue<int> que1;
    while(!que.empty())
    {
        if(que.front()!=remove_element)
            que1.push(que.front());
        que.pop();
    }
    que1.push(insert_element);
    return que1;
}
int select_most_far(int index,queue<int> que)
{
    int a = que.front();
    que.pop();
    int b = que.front();
    que.pop();
    int c = que.front();
    
    srand((unsigned) time(NULL));
    
    int id = rand()%3 + 1;
    switch (id) {
        case 1:
            return a;
        case 2:
            return b;
        case 3:
            return c;
        default:
            break;
    }
    return c;
}

void FIFO()
{
    int queye=1,i;
    cout<<"FIFO"<<endl;
    queue<int> que;
    que.push(page[0]);
    bre=true;
    print(que,page[0]);
    for(i=1;i<size;i++)
    {
        if(!isEqual(que,page[i]))
        {
            bre=true;
            if(que.size()<pageSize)
            {
                que.push(page[i]);
                queye++;
            }
            else
            {
                // 移除最先入队的元素－FIFO
                que.pop();
                que.push(page[i]);
                queye++;
            }
            print(que,page[i]);
        }
        else
        {
            bre=false;
            print(que,page[i]);
        }
    }
    double queyelv=(double)queye/(double)size;
    cout<<"缺页率:"<<queyelv<<endl;
}

void LRU()
{
    int queye=1,i;
    cout<<"LRU"<<endl;
    queue<int> que;
    que.push(page[0]);
    bre=true;
    print(que,page[0]);
    for(i=1;i<size;i++)
    {
        // 发生缺页
        if(!isEqual(que,page[i]))
        {
            bre=true;
            if(que.size()<pageSize)
            {
                que.push(page[i]);
                queye++;
            }
            else
            {
                que.pop();
                que.push(page[i]);
                queye++;
            }
            print(que,page[i]);
        }
        else
        {
            bre = false;
            // 更新队列 把之前用的拿出来，最近用的塞进去 －LRU
            que = LRU_update(que,page[i]);
            print(que,page[i]);
        }
    }
    double queyelv=(double)queye/(double)size;
    cout<<"缺页率:"<<queyelv<<endl;
}

void OPT()
{
    int queye = 1;
    cout<<"OPT"<<endl;
    queue<int> que;
    que.push(page[0]);
    bre = true;
    print(que,page[0]);
    
    for(int i=1;i<size;i++)
    {
        if(!isEqual(que,page[i]))
        {
            bre = true;
            if(que.size() >= pageSize)
            {
                int remove_element = select_most_far(i,que);
                // 插入 ／ 拿出
                que= OPT_update(que,page[i],remove_element);
                print(que,page[i]);
            }
            else
            {
                que.push(page[i]);
                print(que,page[i]);
            }
            queye++;
        }
        else
        {
            bre=false;
            print(que,page[i]);
        }
    }
    double queyelv=(double)queye/(double)size;
    cout<<"缺页率:"<<queyelv<<endl;
}


int main()
{
    int i,select;
    bool judge = true;
    cout<<"输入地址总数    地址序列:"<<endl;
    cin>>size;
    
    for(i = 0;i< size ;i++)
    {
        cin >> mArray[i];
        page[i] = mArray[i] / 100 + 1;
    }
    
    while(judge)
    {
        cout<<"----------------"<<endl;
        cout<<"1:FIFO"<<endl;
        cout<<"2:LRU"<<endl;
        cout<<"3:OPT"<<endl;
        cout<<"4:EXIT"<<endl;
        cout<<"----------------"<<endl;
        cin>>select;
        switch(select)
        {
            case 1:FIFO();
                break;
            case 2:LRU();
                break;
            case 3:OPT();
                break;
            case 4:judge=false;
                break;
        }
    }
    return 0;
}