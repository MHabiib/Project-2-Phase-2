
import { createLocalVue, shallowMount } from '@vue/test-utils'
import Vuex from 'vuex'

import Login from '@/page/login'
// import mockAxios from '../../__mocks__/axios';
import mockAxios from 'jest-mock-axios';

import axios from 'axios';
import VueRouter from 'vue-router';


let wrapper
let store
let actions
let mutations
let state
let router = new VueRouter()
const localVue = createLocalVue()

localVue.use(VueRouter)
localVue.use(Vuex)

beforeEach(()  => {
    window.alert = jest.fn()
    wrapper = shallowMount(Login, {
        propsData: {},
        mocks: { $t: () => {} },
        stubs: {},
        store,
        localVue,   
        router, 
    });
})
afterEach(() => {
    wrapper.destroy()
})
describe('Login.vue',() =>{
    test('setup correctly', ()=>{
        expect(wrapper.text()).toContain('Login')
        expect(wrapper.isVueInstance).toBeTruthy()
    })
    test('btn login clicked',()=>{
        const fakeReturn = {
            name:'fake',
            email:'fake@fake.com'
        }
        wrapper.vm.loginHandler=jest.fn(() =>
            wrapper.vm.response = fakeReturn,
            wrapper.vm.message = 'Welcome!'
        )
        const loginBtn = wrapper.find('button')
        loginBtn.trigger('click')
        wrapper.vm.$nextTick( () => {
            expect(wrapper.vm.loginHandler).toBeCalled()
            expect(wrapper.vm.response).toBe(fakeReturn)
        })
        const expected='Welcome!'
        expect(wrapper.vm.message).toBe(expected)
        
    })
    test('test login input is empty string', ()=>{
        const jsdomAlert = window.alert;  // remember the jsdom alert
        window.alert = () => {}; //provide empty implementation
        wrapper.vm.loginHandler()
        window.alert=jsdomAlert
        const expected='Please input email/password.'
        expect(wrapper.vm.message).toBe(expected)        
    })
    test('login api called', ()=>{
        let dataLogin = {
            email: 'fake@fake.com',
            password: 'fakepassword'
          };
        wrapper.setData(dataLogin)

        
        wrapper.vm.loginHandler()
        wrapper.vm.$router.push = jest.fn()

        let responseObj = {user:{name:'Fake'}}
    
        mockAxios.mockResponse(responseObj)
        expect(mockAxios.post).toHaveBeenCalledWith('http://localhost:8088/auth/signin', dataLogin );
        expect(window.alert).toBeCalled()
        // window.alert=jsdomAlert
        // expect(wrapper.vm.$router.push).toBeCalled()

    })
})

