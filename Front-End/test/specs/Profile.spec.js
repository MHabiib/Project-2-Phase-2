
import { createLocalVue, shallowMount } from '@vue/test-utils'
import Vuex from 'vuex'

import Profile from '@/page/profile'
import {GlobalWithFetchMock} from "jest-fetch-mock";

// import mockAxios from '../../__mocks__/axios';

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
// const mockSuccessResponse = '200 OK'
        
// const mockFetchPromise = {
//         json: () => Promise.resolve(mockSuccessResponse)
// }
beforeEach(()  => {

      // memalsukan fungsi fetch API
      // selalu mengembalikan nilai sesuai yang diinginkan
    
    // global.fetch = jest.fn().mockResolvedValue(mockFetchPromise)
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
        fetch.mockResponse(JSON.stringify({ email: 'fake@mock.com' }))
        expect(wrapper.text()).toContain('Profile')
        expect(wrapper.isVueInstance).toBeTruthy()
    })

    test('function getUserData  called',async (done) =>{

        const expectedResponse = { email: 'fake@mock.com' }
        fetch.mockResponse(JSON.stringify(expectedResponse))

        wrapper.vm.getUserData('email')
        // expect(wrapper.vm.getUserData).toBeCalledTimes(1)
        // expect(wrapper.vm.getUserData).toBeCalledWith('email')
    
        expect(wrapper.vm.currentUser).toEqual(expectedResponse)
        done()
    })
    test('button save showned when button update clicked', ()=>{
        expect(wrapper.contains('.save')).toBe(false)
        expect(wrapper.contains('.cancel')).toBe(false)
        expect(wrapper.find('.profileButton').isVisible()).toBe(true)

        expect(wrapper.vm.editMode).toBe(false)
        
        const btnUpdate = wrapper.find('.profileButton')
        btnUpdate.trigger('click')
        expect(wrapper.contains('.save')).toBe(true)
        expect(wrapper.contains('.cancel')).toBe(true)
        expect(wrapper.find('.profileButton').isVisible()).toBe(false)
        expect(wrapper.vm.editMode).toBe(true)
    })
    test('button cancel clicked', ()=>{
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
        wrapper.setData({
            editMode : true
        })    
        wrapper.vm.cancelChanges = jest.fn()
        const btnCancel = wrapper.find('.cancel')
        btnCancel.trigger('click')
        expect(wrapper.vm.cancelChanges).toBeCalled()
    })
    test('button save clicked', ()=>{
        wrapper.setData({
            editMode : true
        })    
        // wrapper.vm.saveChanges = jest.fn()
        wrapper.vm.validateInput = jest.fn()
        const btnSave = wrapper.find('.save')
        btnSave.trigger('click')
        expect(wrapper.vm.validateInput).toBeCalled()
        // expect(wrapper.vm.saveChanges).toBeCalled()
    })
    test('validate input failed', ()=>{
        const jsdomAlert = window.alert;  // remember the jsdom alert
        window.alert = () => {}; //provide empty implementation
        const newUser = {
                name:'',
                password:'',
                phone:''
        }
        wrapper.setData({
            editMode : true,
            newUser
        })    
        // wrapper.vm.saveChanges = jest.fn()
        wrapper.vm.validateInput()
        window.alert=jsdomAlert

        expect(wrapper.vm.validateInput()).toBe(false)
        
        // expect(wrapper.vm.saveChanges).toBeCalled()
    })
})

