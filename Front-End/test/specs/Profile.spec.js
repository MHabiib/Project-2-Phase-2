
import { createLocalVue, shallowMount } from '@vue/test-utils'
import Vuex from 'vuex'

import Profile from '@/page/profile'
import {GlobalWithFetchMock} from "jest-fetch-mock";


import VueRouter from 'vue-router';
import { getHeapStatistics } from 'v8';


let wrapper
let store
let actions
let mutations
let state
let router = new VueRouter()
const localVue = createLocalVue()
localVue.use(VueRouter)
localVue.use(Vuex)
localVue.filter('dateFormatter',jest.fn())

store = new Vuex.Store({
    modules:{
        getters: jest.fn(),
        actions: jest.fn(),
        state: jest.fn(),
        mutations: jest.fn(),
    }
  })

beforeEach(()  => {
    fetch.resetMocks()
    window.alert=jest.fn()

    const expectedResponse = { email: 'fake@mock.com' }
    fetch.mockResponseOnce(JSON.stringify(expectedResponse))
    wrapper = shallowMount(Profile, {
        stubs:['HeaderSection','SidebarComponent'],
        store,
        localVue,   
        router, 
    });
})
afterEach(() => {
    jest.clearAllMocks()
    wrapper.destroy()
})
describe('Profile.vue',() =>{
    test('setup correctly', ()=>{
        fetch.mockResponseOnce(JSON.stringify({ email: 'fake@mock.com' }))
        expect(wrapper.text()).toContain('Profile')
        expect(wrapper.isVueInstance).toBeTruthy()
    })
    test('function getUserData  called',async (done) =>{
        const expectedResponse = { email: 'fake@mock.com' }
        fetch.mockResponseOnce(JSON.stringify(expectedResponse))

        wrapper.vm.getUserData('email')
        // expect(wrapper.vm.getUserData).toBeCalledTimes(1)
        // expect(wrapper.vm.getUserData).toBeCalledWith('email')
    
        expect(wrapper.vm.currentUser).toEqual(expectedResponse)
        done()
    })
    test('button save showned when button update clicked', ()=>{
        const expectedResponse = { email: 'fake@mock.com' }
        fetch.mockResponseOnce(JSON.stringify(expectedResponse))
        expect(wrapper.contains('.save')).toBe(false)
        expect(wrapper.contains('.cancel')).toBe(false)
        expect(wrapper.find('.profileButton').isVisible()).toBe(true)

        expect(wrapper.vm.editMode).toBe(false)
        
        const btnUpdate = wrapper.find('.profileButton')
        btnUpdate.trigger('click')
        expect(wrapper.contains('.save')).toBe(true)
        expect(wrapper.contains('.cancel')).toBe(true)
        expect(wrapper.text()).toContain('Password')
        expect(wrapper.find('.profileButton').isVisible()).toBe(false)
        expect(wrapper.vm.editMode).toBe(true)
        expect(wrapper.contains('.updateInput')).toBe(true)
    })
    test('button cancel clicked', ()=>{
        const expectedResponse = { email: 'fake@mock.com' }
        fetch.mockResponseOnce(JSON.stringify(expectedResponse))
        wrapper.setData({
            editMode : true
        })        
        expect(wrapper.contains('.save')).toBe(true)
        expect(wrapper.contains('.cancel')).toBe(true)
        expect(wrapper.find('.profileButton').isVisible()).toBe(false)

        expect(wrapper.vm.editMode).toBe(true)
        
        const btnCancel = wrapper.find('.cancel')
        btnCancel.trigger('click')
        expect(wrapper.contains('.save')).toBe(false)
        expect(wrapper.contains('.cancel')).toBe(false)
        expect(wrapper.find('.profileButton').isVisible()).toBe(true)
        expect(wrapper.vm.editMode).toBe(false)
    })
    test('function cancelChanges called button save clicked', ()=>{
        const expectedResponse = { email: 'fake@mock.com' }
        fetch.mockResponseOnce(JSON.stringify(expectedResponse))
        
        wrapper.setData({
            editMode : true
        })    
        wrapper.vm.cancelChanges = jest.fn()
        const btnCancel = wrapper.find('.cancel')
        btnCancel.trigger('click')
        expect(wrapper.vm.cancelChanges).toBeCalled()
    })
    test('button save clicked', ()=>{
        const expectedResponse = { email: 'fake@mock.com' }
        fetch.mockResponseOnce(JSON.stringify(expectedResponse))
        const jsdomAlert = window.alert;  // remember the jsdom alert
        window.alert = () => {}; //provide empty implementation
        window.alert=jsdomAlert
        wrapper.setData({
            editMode : true
        })    

        wrapper.vm.validateInput = jest.fn()
        const btnSave = wrapper.find('.save')
        btnSave.trigger('click')
        expect(wrapper.vm.validateInput).toBeCalled()
    })
    test('validate input failed no name', ()=>{
        const expectedResponse = { email: 'fake@mock.com' }
        fetch.mockResponseOnce(JSON.stringify(expectedResponse))
        const jsdomAlert = window.alert;  // remember the jsdom alert
        window.alert = () => {}; //provide empty implementation
        window.alert=jsdomAlert

        const newUser = {
                name:'',
                password:'whatislove',
                phone:'123456789'
        }
        wrapper.setData({
            editMode : true,
            newUser
        })    
        wrapper.vm.validateInput()
        window.alert=jsdomAlert
        expect(wrapper.vm.validateInput()).toBe(false)      
    })
    test('validate input failed no phone', ()=>{
        const expectedResponse = { email: 'fake@mock.com' }
        fetch.mockResponseOnce(JSON.stringify(expectedResponse))
        const jsdomAlert = window.alert;  // remember the jsdom alert
        window.alert = () => {}; //provide empty implementation
        window.alert=jsdomAlert

        const newUser = {
                name:'Fake Love',
                password:'whatislove',
                phone:''
        }
        wrapper.setData({
            editMode : true,
            newUser
        })    
        wrapper.vm.validateInput()
        expect(wrapper.vm.validateInput()).toBe(false)         
    })
    test('validate input failed no password', ()=>{
        const expectedResponse = { email: 'fake@mock.com' }
        fetch.mockResponseOnce(JSON.stringify(expectedResponse))
        const jsdomAlert = window.alert;  // remember the jsdom alert
        window.alert = () => {}; //provide empty implementation
        window.alert=jsdomAlert

        const newUser = {
                name:'Fake Love',
                password:'123',
                phone:'123456789'
        }
        wrapper.setData({
            editMode : true,
            newUser
        })    
        wrapper.vm.validateInput()
        expect(wrapper.vm.validateInput()).toBe(true)         
    })
    test('validate input password failed', ()=>{
        const expectedResponse = { email: 'fake@mock.com' }
        fetch.mockResponseOnce(JSON.stringify(expectedResponse))
        const newUser = {
                name:'fakelove',
                password:'whatislove',
                phone:'13843458'
        }
        wrapper.setData({
            editMode : true,
            newUser,
            newPassword: 'abc',
            repeatPassword: 'abcd'
        })    
        wrapper.vm.validateInput()
        expect(wrapper.vm.validateInput()).toBe(false)      
    })
    test('validate input success', ()=>{
        const expectedResponse = { email: 'fake@mock.com' }
        fetch.mockResponseOnce(JSON.stringify(expectedResponse))
        const newUser = {
                name:'fakelove',
                password:'whatislove',
                phone:'13843458'
        }
        wrapper.setData({
            editMode : true,
            newUser,
            newPassword: 'abcdef',
            repeatPassword: 'abcdef'
        })    
        wrapper.vm.validateInput()
        expect(wrapper.vm.validateInput()).toBe(true)      
    })
    test('function saveChanges failed',async (done) =>{
        const expectedResponse = { email: 'fake@mock.com' }

        const response = { status: 500 }
        fetch.mockResponseOnce(JSON.stringify(response))
        wrapper.vm.saveChanges()
        expect(wrapper.vm.currentUser).toEqual(expectedResponse)
        done()
    })
    test('cancelChanges called when button clicked', () =>{
        wrapper.setData({
            editMode : true
        })
        const updateBtn = wrapper.find('.cancel')
        wrapper.vm.cancelChanges = jest.fn()
        updateBtn.trigger('click')
        expect(wrapper.vm.cancelChanges).toBeCalled()
    })
})

