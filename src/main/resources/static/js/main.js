function getIndex(list, id) {
    for(var i=0;i<list.length;i++) {
        if (list[i].id=== id){
            return i;
        }
    }
    return -1;
}

var messageApi = Vue.resource('/message{/id}');

 Vue.component('message-form', {
    props:['messages', 'messageAttr'],
    data: function() {
        return {
            name: '',
            id: ''
        }
    },
     watch: {
         messageAttr: function(newVal, oldVal) {
            this.name = newVal.name;
            this.id =newVal.id;
        }
     },
    template:
        '<div>'+
            '<input type="name" placeholder="Write name" v-model="name" />' +
            '<input type="button" value="Save" v-on:click="save" />' +
        '</div>',
    methods: {
        save: function () {
            var message ={ name: this.name};
            if (this.id) {
                messageApi.update({id: this.id}, message).then(result =>
                    result.json().then(data => {
                            var index = getIndex(this.messages, data.id);
                            this.messages.splice(index, 1, data);
                            this.name = ''
                        })
                )
            }else {
                messageApi.save({}, message).then(result =>
                    result.json().then(data => {
                        this.messages.push(data);
                        this.name = ''
                    })
                )
            }
        }
    }
});
Vue.component('message-row', {
    props: ['message', 'editMessage', 'messages'],
    template:'<div>' +
        '<i>({{message.id}})</i>{{message.name}}' +
        '<span style="position: absolute; right: 0">'  +
        '<input type="button" value="Edit" @click="edit"/>' +
        '<input type="button" value="Delete" @click="del"/>' +
        '</span>' +
        '</div>',
    methods: {
        edit: function () {
            this.editMessage(this.message);
        },
        del: function () {
            messageApi.remove({id: this.message.id}).then(result => {
                if (result.ok) {
                    this.messages.splice(this.messages.indexOf(this.message),1 )
                }
            })
        }
    }
});

Vue.component('messages-list',{
    props:['messages'],
    data: function() {
        return {
            message: null
        }
    },
    template:
    '<div style="position: relative; width: 220px;">'+
       '<message-form :messages="messages" :messageAttr="message"/>' +
       '<message-row v-for="message in messages" :k:message="message.id" :message="message" :editMessage="editMessage" :messages="messages"/>'+
    '</div>',
    created: function() {
        messageApi.get().then(result => {
                result.json().then(data => {
                        data.forEach(message => this.messages.push(message))
                    }
                )
            }
        )
    },
    methods: {
        editMessage: function (message) {
            this.message = message;
        }
    }
});

var app = new Vue({
    el: '#app',
    template: '<messages-list :messages="messages"/>',
    data: {
        messages: []
    }
});
