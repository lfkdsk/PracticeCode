#include "link_list.h"

struct symtable *head = 0;

int flag = 0;

struct symtable* addNode(char *symbol){

    if (flag == 0) {
        flag = 1;
        head = (struct symtable*)malloc(sizeof(struct symtable));
        head->name = strdup(symbol);
        head->next = 0;
        return head;
    }

    struct symtable *sp = head;
    
    /* 第1.2次不会进去 */
    while (sp != 0) {
        if (sp->name && !strcmp(sp->name, symbol)) {
          /*  找到一样的了 */
            return sp;
        }

        if (!sp->name) {
            sp->name = strdup(symbol);
            return sp;
        }

        sp = sp->next;
    }
    
    sp = head;
    
    while (sp->next != 0) {
        sp = sp->next;
    }

    struct symtable *tail;
    tail = (struct symtable*)malloc(sizeof(struct symtable));
    tail->name = strdup(symbol);
    tail->next = 0;
    sp->next = tail;
    return tail;
}

void printLink(){
  struct symtable* sp = head;
  while (sp != 0) {
      printf("name : %s index : %p \n", sp->name, sp);
      sp = sp->next;
  }
}
